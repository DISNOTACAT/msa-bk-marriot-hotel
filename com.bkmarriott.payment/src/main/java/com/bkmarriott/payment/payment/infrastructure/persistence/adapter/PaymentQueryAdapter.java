package com.bkmarriott.payment.payment.infrastructure.persistence.adapter;

import com.bkmarriott.payment.payment.application.outputport.PaymentQueryOutputPort;
import com.bkmarriott.payment.payment.domain.Payment;
import com.bkmarriott.payment.payment.infrastructure.persistence.entity.PaymentEntity;
import com.bkmarriott.payment.payment.infrastructure.persistence.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryAdapter implements PaymentQueryOutputPort {

  private final PaymentRepository paymentRepository;

  @Override
  public Optional<Payment> findById(Long paymentId) {
    return paymentRepository.findById(paymentId)
        .map(PaymentEntity::toDomain);
  }
}
