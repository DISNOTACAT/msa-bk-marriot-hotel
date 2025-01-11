package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.Payment;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final InventoryService inventoryService;
    private final ReservationProcessingService reservationProcessingService;
    private final ReservationCommandOutputPort reservationCommandOutputPort;

    public Reservation createReservation(ReservationForCreate reservationForCreate) {
        log.debug("[ReservationService] [createReservation]");

        // 0. 객실 가용 여부 판단
        inventoryService.prepareAvailableRoom(InventoryQuery.fromReservationForCreate(reservationForCreate));

        // 1. 주문 준비
        Reservation savedReservation = reservationProcessingService.prepareReservation(reservationForCreate);

        // 2. 결제 처리
        Payment payment = reservationProcessingService.processPayment(savedReservation, reservationForCreate.paymentForCreate());

        // 3. 예약 상태 변경
        Reservation reservation = reservationCommandOutputPort.updateReservationStatus(savedReservation.getReservationId(), ReservationStatus.PAID);

        // 4. 주문 확정
        reservationProcessingService.confirmReservation(savedReservation, payment);

        return reservation;
    }
}
