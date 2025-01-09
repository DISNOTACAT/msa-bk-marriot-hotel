package com.bkmarriott.reservationservice.reservation.domain;

import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

  private Long reservationId;
  private Long userId;
  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;
  private RoomType roomType;
  private ReservationStatus status;
  private Long roomId;
}
