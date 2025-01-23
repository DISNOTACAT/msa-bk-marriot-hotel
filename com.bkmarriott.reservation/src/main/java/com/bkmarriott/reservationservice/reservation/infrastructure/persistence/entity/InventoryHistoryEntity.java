package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.util.RedisKeyParser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name  = "m_inventory_history")
@Entity
@Builder
public class InventoryHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventEntityType eventType;

    @Column(name = "sequence_number", nullable = false)
    private Long sequenceNumber;

    @Column(name = "redis_room_key", nullable = false)
    private String redisRoomKey;

    @Column(name = "room_stock", nullable = false)
    private Long roomStock;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomEntityType roomType;


    public static List<InventoryHistoryEntity> toEntity(RoomInventoryEvent event) {
        return event.getInventoryInfoList().stream()
                .map(info -> {
                    RedisKeyParser.ParsedKey parsedKey = RedisKeyParser.parseInventoryKey(info.getRedisRoomKey());
                    return new InventoryHistoryEntity(
                            null,
                            EventEntityType.fromDomain(event.getChangeType()),
                            info.getSequenceNumber(),
                            info.getRedisRoomKey(),
                            info.getRoomStock(),
                            parsedKey.hotelId(),
                            parsedKey.date(),
                            parsedKey.roomType()
                    );
                })
                .toList();
    }

}
