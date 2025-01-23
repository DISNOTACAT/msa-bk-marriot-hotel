package com.bkmarriott.reservationservice.reservation.infrastructure.batch.step;

import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.infrastructure.batch.exception.DataIntegrityException;
import com.bkmarriott.reservationservice.reservation.infrastructure.batch.item.EventTypeCount;
import com.bkmarriott.reservationservice.reservation.infrastructure.batch.item.RoomKeyCheckResult;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.*;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryHistoryRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.util.RedisKeyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InventorySyncStepConfig {private final DataSource dataSource;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final InventoryRepository inventoryRepository;

    @Bean
    public Step validateAndSyncInventoryStep(){
        return new StepBuilder("syncInventory", jobRepository)
                .<InventoryHistoryEntity, RoomKeyCheckResult>chunk(10, transactionManager)
                .reader(distinctRoomKeyReader())
                .processor(historyReplayProcessor())
                .writer(inventorySyncWriter())
                .faultTolerant()
                .skip(ResourceNotFoundException.class)
                .skip(DataIntegrityException.class)
//                .skipLimit(10)
                .listener(new SkipListener<InventoryHistoryEntity, RoomKeyCheckResult>() {
                    @Override
                    public void onSkipInProcess(InventoryHistoryEntity item, Throwable t) {
                        log.warn("[SkipListener] Skipped item: {} due to error: {}", item.getRedisRoomKey(), t.getMessage());
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<InventoryHistoryEntity> distinctRoomKeyReader(){
        JdbcCursorItemReader<InventoryHistoryEntity> reader = new JdbcCursorItemReader<>();
        reader.setFetchSize(10);
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM m_inventory_history");
        reader.setRowMapper((rs, rowNum) -> InventoryHistoryEntity.builder()
                .id(rs.getLong("id"))
                .eventType(EventEntityType.valueOf(rs.getString("event_type")))
                .sequenceNumber(rs.getLong("sequence_number"))
                .redisRoomKey(rs.getString("redis_room_key"))
                .roomStock(rs.getLong("room_stock"))
                .hotelId(rs.getLong("hotel_id"))
                .date(rs.getObject("date", LocalDate.class))
                .roomType(RoomEntityType.valueOf(rs.getString("room_type")))
                .build());
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<InventoryHistoryEntity, RoomKeyCheckResult> historyReplayProcessor(){
        return inventoryHistoryEntity ->{
            log.info("[InventorySyncStepConfig] [historyReplayProcessor] id ::: {}, sequenceNum ::: {}, redisKey ::: {}, roomStock ::: {}", inventoryHistoryEntity.getId(), inventoryHistoryEntity.getSequenceNumber(), inventoryHistoryEntity.getRedisRoomKey(), inventoryHistoryEntity.getRoomStock());

            RoomTypeInventoryEntity inventoryEntity = inventoryRepository.findById(RoomTypeInventoryId.of(inventoryHistoryEntity.getHotelId(), inventoryHistoryEntity.getDate(), inventoryHistoryEntity.getRoomType().toDomain()))
                    .orElseThrow(() -> new ResourceNotFoundException("배치 처리중 해당하는 인벤토리 정보가 없습니다."));

            List<EventTypeCount> eventTypeCounts = inventoryHistoryRepository
                    .countByRedisRoomKeyAndSequenceNumberLessThanGroupByEventType(inventoryHistoryEntity.getRedisRoomKey(), inventoryHistoryEntity.getSequenceNumber());

            AtomicLong historyCount = new AtomicLong(inventoryHistoryEntity.getRoomStock());
            eventTypeCounts.forEach(count -> {
                log.info("[InventorySyncStepConfig] [historyReplayProcessor] type ::: {}, count ::: {}", count.type(), count.count());
                historyCount.addAndGet((count.type() == EventEntityType.PREPARED) ? count.count() : -count.count());
            });

            if((long) inventoryEntity.getTotalInventory() != historyCount.get()){
                log.warn("[InventorySyncStepConfig] [historyReplayProcessor] Occur DataIntegrityException  SequenceNumber ::: {}, RedisRoomStock ::: {}, Expected ::: {}, Actual ::: {} ", inventoryHistoryEntity.getSequenceNumber(), inventoryHistoryEntity.getRoomStock(), inventoryEntity.getTotalInventory(), historyCount.get());
                throw new DataIntegrityException("History 테이블과 해당 시기 Redis 값이 일치 하지 않습니다.");
            }

            return new RoomKeyCheckResult(inventoryHistoryEntity.getRedisRoomKey(), inventoryHistoryEntity.getRoomStock());
        };
    }

    @Bean
    @StepScope
    public ItemWriter<RoomKeyCheckResult> inventorySyncWriter() {
        return items -> {
            for (RoomKeyCheckResult result : items) {
                RedisKeyParser.ParsedKey parsedKey = RedisKeyParser.parseInventoryKey(result.getRedisRoomKey());
                inventoryRepository.syncTotalReserved(parsedKey.hotelId(), parsedKey.date(), parsedKey.roomType(), (int) result.getRoomStock());
            }
        };
    }
}
