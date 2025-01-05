package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import com.bkmarriott.reservationservice.reservation.presentation.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationQueryAdaptor {

  private final ReservationRepository reservationRepository;

  public ReservationEntity findById(Long hotelId) {
    return reservationRepository.findById(hotelId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 정보"));
  }
}
