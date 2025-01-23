package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import com.bkmarriott.reservationservice.reservation.infrastructure.batch.item.EventTypeCount;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.InventoryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryHistoryRepository extends JpaRepository<InventoryHistoryEntity, Long> {

    @Query("""
        SELECT new com.bkmarriott.reservationservice.reservation.infrastructure.batch.item.EventTypeCount(
            e.eventType, COUNT(e)
        )
        FROM InventoryHistoryEntity e
        WHERE e.redisRoomKey = :redisRoomKey
          AND e.sequenceNumber <= :sequenceNumber
        GROUP BY e.eventType
    """)
    List<EventTypeCount> countByRedisRoomKeyAndSequenceNumberLessThanGroupByEventType(
            @Param("redisRoomKey") String redisRoomKey,
            @Param("sequenceNumber") Long sequenceNumber
    );

}
