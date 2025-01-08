package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.reservation;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreateReservation {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RoomType roomType;

    public ReservationForCreate toDomain(String userId) {
      return new ReservationForCreate(
          Long.parseLong(userId),
          hotelId,
          startDate,
          endDate,
          roomType
      );
    }
  }


  @Getter
  @AllArgsConstructor
  public static class Response {
    private Long reservationId;
    private Long userId;
    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RoomType roomType;
    private ReservationStatus status;

    public static Response from(Reservation reservation) {
      return new Response(
          reservation.getReservationId(),
          reservation.getUserId(),
          reservation.getHotelId(),
          reservation.getStartDate(),
          reservation.getEndDate(),
          reservation.getRoomType(),
          reservation.getStatus()
      );
    }
  }

}
