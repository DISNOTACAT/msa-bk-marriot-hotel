package com.bkmarriott.payment.application.outputport;

import com.bkmarriott.payment.domain.Payment;

public interface PaymentCommandOutputPort {

  Payment save(Payment payment);

  Payment refund(Long paymentId);
}
