package com.bkmarriott.reservationservice.reservation.application.outputport.reservation;


import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;

public interface ReservationCommandOutputPort {

  Reservation save(ReservationForCreate reservationForCreate);
}
