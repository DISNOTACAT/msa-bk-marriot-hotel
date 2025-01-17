package com.bkmarriott.reservationservice.reservation.application.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableRoomsWithChargeDto {

  private RoomType roomType;
  private int roomCount;
  private int charge;
}
