package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.reservation;

import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationQueryAdaptor implements ReservationQueryOutputPort {

  private final ReservationRepository reservationRepository;

  @Override
  public Optional<Reservation> findById(Long reservationId) {
    return reservationRepository.findById(reservationId)
        .map(ReservationEntity::toDomain);
  }
}
