package com.bkmarriott.payment.payment.application.service;

import com.bkmarriott.payment.payment.application.outputport.PaymentCommandOutputPort;
import com.bkmarriott.payment.payment.application.outputport.PaymentQueryOutputPort;
import com.bkmarriott.payment.payment.domain.Payment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentCommandOutputPort paymentCommandOutputPort;
  private final PaymentQueryOutputPort paymentQueryOutputPort;

  public Payment createPayment(Payment payment) {
    // 외부 PG 결제 요청 및 저장
    String transactionId = UUID.randomUUID().toString();
    payment.setPaid(transactionId);
    return paymentCommandOutputPort.save(payment);
  }

  public Payment updateReservationId(Long paymentId, Long reservationId) {
    return paymentCommandOutputPort.updateReservationId(paymentId, reservationId);
  }
}
