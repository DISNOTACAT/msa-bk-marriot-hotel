package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomsWithChargeDto;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
public record InventorySearchResponse (
    RoomType roomType,
    int roomCount,
    int charge
) {
  private static final double ROOM_SPACE_POLICY = 1.1;

  @Builder(builderMethodName = "builderWithRoomPolicy")
  public InventorySearchResponse(RoomType roomType, int roomCount, int charge) {
    this.roomType = roomType;
    this.roomCount = (int) Math.floor(roomCount * ROOM_SPACE_POLICY);
    this.charge = charge;
  }

  public static InventorySearchResponse from(
      AvailableRoomsWithChargeDto chargeDto) {
    return InventorySearchResponse.builderWithRoomPolicy()
        .roomType(chargeDto.getRoomType())
        .roomCount(chargeDto.getRoomCount())
        .charge(chargeDto.getCharge())
        .build();
  }
}