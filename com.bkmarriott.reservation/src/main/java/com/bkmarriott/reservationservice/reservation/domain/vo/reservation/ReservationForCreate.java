package com.bkmarriott.reservationservice.reservation.domain.vo.reservation;

import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationForCreate {

  private Long userId;
  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;
  private RoomType roomType;
  private ReservationStatus status;

  public ReservationForCreate(Long userId, Long hotelId, LocalDate startDate,
      LocalDate endDate, RoomType roomType) {
    this.userId = userId;
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.roomType = roomType;
    this.status = ReservationStatus.PENDING;
  }

  public void setPaid() {
    this.status = ReservationStatus.PAID;
  }
}
