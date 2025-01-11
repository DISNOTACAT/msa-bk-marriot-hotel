package com.bkmarriott.reservationservice.reservation.infrastructure.cache.adapter;

import com.bkmarriott.reservationservice.reservation.application.exception.NoAvailableRoomsException;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.cache.util.RedisKeyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class InventoryCacheAdapter implements InventoryCacheOutputPort {

    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public void decreaseRoomCount(InventoryQuery query) {
        log.info("[InventoryCacheAdapter] [decreaseRoomCount]");
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();

        List<String> pastKeys = new ArrayList<>();
        for (LocalDate date : query.getDateRange()) {
            String key = RedisKeyFactory.generateInventoryKey(query.hotelId(), date, query.roomType());
            pastKeys.add(key);
            if (ops.decrement(key) < 0) {
                for(String pastKey : pastKeys){
                    ops.increment(pastKey);
                }
                // TODO 객실 선점 실패 히스토리 저장
                throw new NoAvailableRoomsException("객실 선점에 실패했습니다.");
            }
        }
    }

    @Override
    public void rollbackCount(InventoryQuery query) {
        log.info("[InventoryCacheAdapter] [rollbackCount]");
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        query.getDateRange().forEach(date -> {
            String key = RedisKeyFactory.generateInventoryKey(query.hotelId(), date, query.roomType());
            ops.increment(key);
        });
    }
}