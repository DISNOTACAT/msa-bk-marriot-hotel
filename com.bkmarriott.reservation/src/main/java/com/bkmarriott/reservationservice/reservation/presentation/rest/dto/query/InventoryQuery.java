package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryQuery {

  private static final double ROOM_SPACE_POLICY = 1.1;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Builder
  public static class Request {

    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;


  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Builder
  public static class Response {

    private RoomType roomType;
    private int quantity;


    @QueryProjection
    public Response(RoomEntityType roomType, int quantity) {
      this.roomType = roomType.toDomain();
      this.quantity = (int) Math.floor(quantity * ROOM_SPACE_POLICY);
    }
  }


}
