package com.bkmarriott.reservationservice.reservation.application.outputport.reservation;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import java.util.Optional;

public interface ReservationQueryOutputPort {

  Optional<Reservation> findById(Long reservationId);
}
