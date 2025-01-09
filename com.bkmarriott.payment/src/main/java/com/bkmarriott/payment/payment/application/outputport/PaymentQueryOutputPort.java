package com.bkmarriott.payment.payment.application.outputport;

import com.bkmarriott.payment.payment.domain.Payment;
import java.util.Optional;

public interface PaymentQueryOutputPort {

  Optional<Payment> findById(Long paymentId);
}
