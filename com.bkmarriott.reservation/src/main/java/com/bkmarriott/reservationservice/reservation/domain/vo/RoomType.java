package com.bkmarriott.reservationservice.reservation.domain.vo;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import java.util.Objects;

public enum RoomType {
  STANDARD, TWIN, DELUXE, SUITE;


  public RoomEntityType toEntity() {
    for(RoomEntityType roomType : RoomEntityType.values()) {
      if(Objects.equals(roomType.name(), this.name())) {
        return roomType;
      }
    }
    throw new IllegalArgumentException("Invalid type: " + this.name());
  }
}
