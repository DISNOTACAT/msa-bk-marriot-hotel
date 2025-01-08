package com.bkmarriott.payment.payment.infrastructure.persistence.adapter;

import com.bkmarriott.payment.payment.application.outputport.PaymentCommandOutputPort;
import com.bkmarriott.payment.payment.domain.Payment;
import com.bkmarriott.payment.payment.infrastructure.persistence.entity.PaymentEntity;
import com.bkmarriott.payment.payment.infrastructure.persistence.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PaymentCommandAdapter implements PaymentCommandOutputPort {

  private final PaymentRepository paymentRepository;

  @Override
  public Payment save(Payment payment) {
    return paymentRepository.save(PaymentEntity.fromDomain(payment)).toDomain();
  }
}
