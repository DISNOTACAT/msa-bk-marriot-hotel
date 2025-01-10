package com.bkmarriott.payment.application.outputport;

import com.bkmarriott.payment.domain.Payment;
import java.util.Optional;

public interface PaymentQueryOutputPort {

  Optional<Payment> findById(Long paymentId);
}
