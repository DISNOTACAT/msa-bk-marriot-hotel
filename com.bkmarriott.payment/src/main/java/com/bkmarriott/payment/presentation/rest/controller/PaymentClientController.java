package com.bkmarriott.payment.presentation.rest.controller;

import com.bkmarriott.payment.application.dto.CreatePaymentDto;
import com.bkmarriott.payment.application.service.PaymentService;
import com.bkmarriott.payment.presentation.rest.dto.PaymentDto;
import com.bkmarriott.payment.presentation.rest.dto.PaymentRequestDto;
import com.bkmarriott.payment.presentation.rest.dto.auth.Actor;
import com.bkmarriott.payment.presentation.rest.util.auth.LoginActor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentClientController {

  private final PaymentService paymentService;

  @PostMapping("/api/v1/payments")
  public PaymentDto processPayment(@RequestBody PaymentRequestDto request, @LoginActor Actor actor) {
    return PaymentDto.from(paymentService.createPayment(CreatePaymentDto.from(request, actor.userId())));
  }

  @PatchMapping("/api/v1/payments/refund/{paymentId}")
  public PaymentDto refundPayment(@PathVariable Long paymentId, @LoginActor Actor actor) {
    return PaymentDto.from(paymentService.refundPayment(paymentId, actor.userId()));
  }
}
