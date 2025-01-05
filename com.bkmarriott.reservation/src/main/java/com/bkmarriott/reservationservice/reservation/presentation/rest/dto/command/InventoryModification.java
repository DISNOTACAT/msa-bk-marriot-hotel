package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.command;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InventoryModification {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Builder
  public static class Response {

    private Long hotelId;
    private LocalDate date;
    private RoomType roomType;
    private int totalInventory;
    private int totalReserved;

    public static Response from(Inventory inventory) {
      return Response.builder()
          .hotelId(inventory.getHotelId())
          .date(inventory.getDate())
          .roomType(inventory.getRoomType())
          .totalInventory(inventory.getTotalInventory())
          .totalReserved(inventory.getTotalReserved())
          .build();
    }
  }


}
