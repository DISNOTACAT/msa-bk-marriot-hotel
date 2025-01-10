package com.bkmarriott.payment.application.outputport;

import com.bkmarriott.payment.domain.Payment;

public interface PaymentCommandOutputPort {

  Payment save(Payment payment, Long userId);

  Payment refund(Long paymentId, Long userId);
}
