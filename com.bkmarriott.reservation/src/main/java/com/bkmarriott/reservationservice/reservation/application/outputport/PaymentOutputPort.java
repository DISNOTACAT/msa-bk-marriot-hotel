package com.bkmarriott.reservationservice.reservation.application.outputport;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.CreatePayment.Requset;

public interface PaymentOutputPort {

  Long createPayment(Requset from);

  void updateReservationId(Long paymentId, Long reservationId);
}
