package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
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
        WHERE rt.id = :id
        """)
  void increaseReserved(RoomTypeInventoryId id);

  @Modifying
  @Query("""
        UPDATE RoomTypeInventoryEntity rt
        SET rt.totalReserved = rt.totalReserved - 1
        WHERE rt.id = :id
        """)
  void decreaseReserved(RoomTypeInventoryId id);
}
