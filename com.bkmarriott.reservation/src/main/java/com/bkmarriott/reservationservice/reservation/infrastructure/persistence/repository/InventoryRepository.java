package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<RoomTypeInventoryEntity, RoomTypeInventoryId> {

  @Modifying
  @Query("""
        UPDATE RoomTypeInventoryEntity rt
        SET rt.totalReserved = rt.totalReserved + 1
        WHERE rt.id.date between :startDate and :endDate
        AND rt.id.hotelId = :hotelId
        AND rt.id.roomType = :roomType
        """)
  void increaseReserved(Long hotelId, RoomEntityType roomType, LocalDate startDate, LocalDate endDate);

  @Modifying
  @Query("""
        UPDATE RoomTypeInventoryEntity rt
        SET rt.totalReserved = rt.totalReserved - 1
        WHERE rt.id.date between :startDate and :endDate
        AND rt.id.hotelId = :hotelId
        AND rt.id.roomType = :roomType
        """)
  void decreaseReserved(Long hotelId, RoomEntityType roomType, LocalDate startDate, LocalDate endDate);
}
