package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableInventoryWithChargeDto;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryQuery {

  private static final double ROOM_SPACE_POLICY = 1.1;

  @Getter
  @NoArgsConstructor
  @Builder
  public static class Response {

    private RoomType roomType;
    private int quantity;
    private int charge;

    public Response(RoomType roomType, int quantity, int charge) {
      this.roomType = roomType;
      this.quantity = (int) Math.floor(quantity * ROOM_SPACE_POLICY);
      this.charge = charge;
    }

    public static Response from(
        AvailableInventoryWithChargeDto chargeDto) {
      return new Response(chargeDto.getRoomType(), chargeDto.getQuantity(), chargeDto.getCharge());
    }
  }
}
