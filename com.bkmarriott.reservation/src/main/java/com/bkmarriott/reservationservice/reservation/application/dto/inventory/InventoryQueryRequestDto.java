package com.bkmarriott.reservationservice.reservation.application.dto.inventory;

import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class InventoryQueryRequestDto {

  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;
  private RoomType roomType;

  public InventoryQueryRequestDto(Long hotelId, LocalDate startDate, LocalDate endDate) {
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public InventoryQueryRequestDto(Long hotelId, LocalDate startDate,
      LocalDate endDate, RoomType roomType) {
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.roomType = roomType;
  }
}
