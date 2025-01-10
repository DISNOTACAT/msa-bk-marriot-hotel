package com.bkmarriott.reservationservice.reservation.application.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableInventoryWithChargeDto {

  private RoomType roomType;
  private int quantity;
  private int charge;
}
