package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.command;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InventoryModification {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Response {

    private Long hotelId;
    private LocalDate date;
    private RoomType roomType;
    private int totalInventory;
    private int totalReserved;

    public static Response from(Inventory inventory) {
      return new Response(
          inventory.getHotelId(),
          inventory.getDate(),
          inventory.getRoomType(),
          inventory.getTotalInventory(),
          inventory.getTotalReserved());
    }
  }


}
