package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationStatus;
import java.util.Objects;

public enum ReservationEntityStatus {

  PENDING, PAID, REFUNDED, CANCELLED, REJECTED;

  public static ReservationEntityStatus fromDomain(ReservationStatus status) {
    return ReservationEntityStatus.valueOf(status.name());
  }

  public ReservationStatus toDomain() {
    for(ReservationStatus status : ReservationStatus.values()) {
      if(Objects.equals(status.name(), this.name())) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid status: " + this.name());
  }
}
