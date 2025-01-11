package com.bkmarriott.reservationservice.reservation.application.outputport.feign;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.Payment;
import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;

public interface PaymentOutputPort {

    Payment processPayment(PaymentForCreate paymentForCreate, Reservation reservationInfo);

    Payment processRefund(Long paymentId, Reservation reservationInfo);
}
