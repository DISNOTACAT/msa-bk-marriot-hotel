package com.bkmarriott.payment.infrastructure.persistence.adapter;

import com.bkmarriott.payment.application.outputport.PaymentCommandOutputPort;
import com.bkmarriott.payment.domain.Payment;
import com.bkmarriott.payment.infrastructure.persistence.entity.PaymentEntity;
import com.bkmarriott.payment.infrastructure.persistence.repository.PaymentRepository;
import com.bkmarriott.payment.presentation.rest.exception.ResourceNotFoundException;
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
    return paymentRepository.save(PaymentEntity.from(payment)).toDomain();
  }

  @Override
  public Payment refund(Long paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 결제 정보"))
        .setRefunded().toDomain();
  }
}
