package com.bkmarriott.reservationservice.reservation.application.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class InventoryQueryRequestDto {

  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;

  public InventoryQueryRequestDto(Long hotelId, LocalDate startDate, LocalDate endDate) {
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
  }

}
