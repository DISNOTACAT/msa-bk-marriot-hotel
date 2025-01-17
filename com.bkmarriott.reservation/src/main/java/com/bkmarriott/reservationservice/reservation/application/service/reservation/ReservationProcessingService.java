package com.bkmarriott.reservationservice.reservation.application.service.reservation;

import com.bkmarriott.reservationservice.reservation.application.exception.PaymentException;
import com.bkmarriott.reservationservice.reservation.application.exception.ReservationProcessingException;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryService;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationProcessingService {

    private final InventoryService inventoryService;
    private final ReservationCommandOutputPort reservationCommandOutputPort;
    private final CouponOutputPort couponOutputPort;
    private final PaymentOutputPort paymentOutputPort;
    private final InventoryCacheOutputPort inventoryCacheOutputPort;

    public Reservation prepareReservation(ReservationForCreate reservationForCreate){
        log.debug("[ReservationProcessingService] [prepareReservation]");
        try {
            // 1. 쿠폰 유효성 검증
            Optional.ofNullable(reservationForCreate.paymentForCreate().appliedCoupon()).ifPresent(couponOutputPort::verifyCoupon);
            // 2. 예약 정보 DB 저장 및 예약 번호 반환 (PENDING)
            return reservationCommandOutputPort.createReservation(reservationForCreate);

        } catch (Exception e) {
            log.error("[ReservationProcessingService] [prepareReservation] Error occurred: {}", e.getMessage());
            // Redis Count 수정
            inventoryCacheOutputPort.rollbackCount(InventoryQuery.fromReservationForCreate(reservationForCreate));
            throw new ReservationProcessingException("예약 준비 중 오류가 발생했습니다.");
        }
    }

    public Payment processPayment(Reservation reservation, PaymentForCreate paymentForCreate){
        log.info("[ReservationProcessingService] [processPayment] reservationId ::: {}", reservation.getReservationId());
        try {
            // 1. Payment 서비스 호출
            return paymentOutputPort.processPayment(paymentForCreate, reservation); // FeignClient
        } catch (Exception e){
            log.error("[ReservationProcessingService] [processPayment] Error occurred {}: {}", reservation.getReservationId(), e.getMessage());
            // 상태 수정
            reservationCommandOutputPort.updateReservationStatus(reservation.getReservationId(), ReservationStatus.CANCELLED);
            // Redis Count 수정
            inventoryCacheOutputPort.rollbackCount(InventoryQuery.fromReservation(reservation));
            throw new PaymentException("결제가 진행되지 않았습니다.");
        }
    }

    public void confirmReservation(Reservation reservation, Payment payment){
        log.info("[ReservationProcessingService] [confirmReservation] reservationId ::: {}", reservation.getReservationId());
        try{
            // 1. Inventory 수정
            inventoryService.updateTotalReservedInventory(reservation.getReservationId());

            // 2. 쿠폰 사용 처리
            Optional.ofNullable(payment.appliedCoupon()).ifPresent(couponOutputPort::useCoupon);

        }catch (Exception e){
            log.error("[ReservationProcessingService] [confirmReservation] Error occurred {}: {}", reservation.getReservationId(), e.getMessage());
            // Redis Count 수정
            inventoryCacheOutputPort.rollbackCount(InventoryQuery.fromReservation(reservation));
            // 환불 처리
            paymentOutputPort.processRefund(payment.paymentId(), reservation);
            // 상태 변경
            reservationCommandOutputPort.updateReservationStatus(reservation.getReservationId(), ReservationStatus.REFUNDED);
            throw new ReservationProcessingException("예약 확정 중 오류가 발생했습니다.");
        }
    }
}