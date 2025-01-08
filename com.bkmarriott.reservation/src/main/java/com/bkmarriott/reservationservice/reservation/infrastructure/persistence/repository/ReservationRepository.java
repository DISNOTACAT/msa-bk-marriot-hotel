package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

}
