package com.bkmarriott.reservationservice.reservation.application.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class InventoryQueryResponseDto {

  private RoomType roomType;
  private int quantity;

  @QueryProjection
  public InventoryQueryResponseDto(RoomEntityType roomType, int quantity) {
    this.roomType = roomType.toDomain();
    this.quantity = quantity;
  }
}
