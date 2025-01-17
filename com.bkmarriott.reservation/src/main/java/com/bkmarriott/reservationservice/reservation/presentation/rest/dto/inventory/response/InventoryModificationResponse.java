package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;

public record InventoryModificationResponse(
    Long hotelId,
    LocalDate date,
    RoomType roomType,
    int totalInventory,
    int totalReserved
) {
    public static InventoryModificationResponse from(Inventory inventory) {
      return new InventoryModificationResponse(
          inventory.getHotelId(),
          inventory.getDate(),
          inventory.getRoomType(),
          inventory.getTotalInventory(),
          inventory.getTotalReserved());
    }
}
