package com.bkmarriott.reservationservice.reservation.application.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class AvailableRoomCountDto {

  private RoomType roomType;
  private int count;

  @QueryProjection
  public AvailableRoomCountDto(RoomEntityType roomType, int count) {
    this.roomType = roomType.toDomain();
    this.count = count;
  }
}
