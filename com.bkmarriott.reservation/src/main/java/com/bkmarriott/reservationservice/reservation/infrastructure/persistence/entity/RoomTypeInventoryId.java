package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class RoomTypeInventoryId implements Serializable {

  @Column(name = "hotel_id", nullable = false)
  private Long hotelId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(name = "room_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private RoomEntityType roomType;

  public RoomTypeInventoryId(Long hotelId, LocalDate date,
      RoomEntityType roomType) {
    this.hotelId = hotelId;
    this.date = date;
    this.roomType = roomType;
  }

  public static RoomTypeInventoryId of(Long hotelId, LocalDate date, RoomType roomType) {
    return new RoomTypeInventoryId(hotelId,date,RoomEntityType.fromDomain(roomType));
  }

  public static RoomTypeInventoryId from(Inventory inventory) {
    return new RoomTypeInventoryId(inventory.getHotelId(),inventory.getDate(),RoomEntityType.fromDomain(inventory.getRoomType()));
  }
}
