package com.bkmarriott.reservationservice.reservation.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InventorySearchRequestDto{
  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;

  @Builder
  public InventorySearchRequestDto(Long hotelId, LocalDate startDate, LocalDate endDate) {
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
