package com.bkmarriott.payment.payment.application.outputport;

import com.bkmarriott.payment.payment.domain.Payment;

public interface PaymentCommandOutputPort {

  Payment save(Payment payment);

  Payment updateReservationId(Long paymentId,Long reservationId);
}
