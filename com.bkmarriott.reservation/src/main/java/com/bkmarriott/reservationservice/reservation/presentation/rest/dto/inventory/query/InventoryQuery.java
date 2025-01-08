package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.query;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryQuery {

  private static final double ROOM_SPACE_POLICY = 1.1;

  @Getter
  @NoArgsConstructor
  public static class Request {

    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Request(Long hotelId, LocalDate startDate, LocalDate endDate) {
      this.hotelId = hotelId;
      this.startDate = startDate;
      this.endDate = endDate;
    }
  }

  @Getter
  @NoArgsConstructor
  @Builder
  public static class Response {

    private RoomType roomType;
    private int quantity;

    public Response(RoomType roomType, int quantity) {
      this.roomType = roomType;
      this.quantity = (int) Math.floor(quantity * ROOM_SPACE_POLICY);
    }

    public static Response from(
        InventoryQueryResponseDto responseDto) {
      return new Response(responseDto.getRoomType(), responseDto.getQuantity());
    }
  }
}
