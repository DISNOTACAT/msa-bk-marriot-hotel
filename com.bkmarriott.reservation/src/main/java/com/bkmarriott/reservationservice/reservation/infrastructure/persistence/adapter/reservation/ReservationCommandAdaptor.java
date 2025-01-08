package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.reservation;

import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationCommandAdaptor implements ReservationCommandOutputPort {

  private final ReservationRepository reservationRepository;

  @Override
  public Reservation save(ReservationForCreate reservationForCreate) {
    return reservationRepository.save(ReservationEntity.fromDomain(reservationForCreate)).toDomain();
  }
}
