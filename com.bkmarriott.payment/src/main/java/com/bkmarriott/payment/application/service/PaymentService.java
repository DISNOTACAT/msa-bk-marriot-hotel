package com.bkmarriott.payment.application.service;

import com.bkmarriott.payment.application.dto.CreatePaymentDto;
import com.bkmarriott.payment.application.exception.SavePaymentFailureException;
import com.bkmarriott.payment.application.outputport.PaymentCommandOutputPort;
import com.bkmarriott.payment.application.outputport.PaymentQueryOutputPort;
import com.bkmarriott.payment.domain.Payment;
import com.bkmarriott.payment.presentation.rest.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentCommandOutputPort paymentCommandOutputPort;
  private final PaymentQueryOutputPort paymentQueryOutputPort;

  public Payment createPayment(CreatePaymentDto createPaymentDto) {
    Payment payment = Payment.from(createPaymentDto);
    String transactionId = UUID.randomUUID().toString();
    payment.setPaid(transactionId);
    log.info("[PaymentService] [createPayment] transactionId ::: {}", transactionId);
    try {
      return paymentCommandOutputPort.save(payment);
    } catch (Exception e) {
      log.debug("[PaymentService] [createPayment] paymentId ::: {}, exception ::: {}", transactionId, e.getMessage());
      throw new SavePaymentFailureException("결제 정보 생성에 실패하였습니다.");
    }
  }

  public Payment refundPayment(Long paymentId) {
    Payment payment = paymentQueryOutputPort.findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 결제 정보"));
    log.info("[PaymentService] [refundPayment] Refund payment ::: {}", payment.getId());
    return paymentCommandOutputPort.refund(paymentId);

  }
}
