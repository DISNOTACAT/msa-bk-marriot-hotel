package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<RoomTypeInventoryEntity, RoomTypeInventoryId> {

  @Modifying
  @Query("""
      UPDATE RoomTypeInventoryEntity rt
      SET rt.totalReserved = rt.totalReserved + 1
      WHERE rt.id.date BETWEEN :#{#reservation.startDate} AND :#{#reservation.endDate}
      AND rt.id.hotelId = :#{#reservation.hotelId}
      AND rt.id.roomType = :#{#reservation.roomType.toEntity()}
      """)
  void increaseReserved(Reservation reservation);

  @Modifying
  @Query("""
      UPDATE RoomTypeInventoryEntity rt
      SET rt.totalReserved = rt.totalReserved - 1
      WHERE rt.id.date BETWEEN :#{#reservation.startDate} AND :#{#reservation.endDate}
      AND rt.id.hotelId = :#{#reservation.hotelId}
      AND rt.id.roomType = :#{#reservation.roomType.toEntity()}
      """)
  void decreaseReserved(Reservation reservation);



  @Modifying
  @Query("""
      UPDATE RoomTypeInventoryEntity rt
      SET rt.totalReserved = rt.totalInventory - :calculatedStock
      WHERE rt.id.date = :dateTime
      AND rt.id.hotelId = :hotelId
      AND rt.id.roomType = :roomType
      """)
  void syncTotalReserved(@Param("hotelId") Long hotelId,
                         @Param("dateTime") LocalDate dateTime,
                         @Param("roomType") RoomEntityType roomType,
                         @Param("calculatedStock") int calculatedStock);

}
