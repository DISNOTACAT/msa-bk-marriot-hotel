package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.application.exception.inventory.RoomTypeInventoryEntityException;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "m_room_type_inventory")
@Entity
public class RoomTypeInventoryEntity extends BaseEntity {

  @EmbeddedId
  private RoomTypeInventoryId id;

  @Column(name = "total_inventory", nullable = false)
  private int totalInventory;

  @Column(name = "total_reserved", nullable = false)
  private int totalReserved;

  public Inventory toDomain() {
    return new Inventory(
        id.getHotelId(),
        id.getDate(),
        id.getRoomType().toDomain(),
        totalInventory,
        totalReserved);
  }

  public RoomTypeInventoryEntity increaseReserved() {

    if(totalInventory == totalReserved) {
      throw new RoomTypeInventoryEntityException("예약 가능한 객실이 없습니다.");
    }

    this.totalReserved++;
    return this;
  }

  public RoomTypeInventoryEntity decreaseReserved() {

    if(totalReserved == 0) {
      throw new RoomTypeInventoryEntityException("예약된 객실이 없습니다.");
    }

    this.totalReserved--;
    return this;
  }
}
