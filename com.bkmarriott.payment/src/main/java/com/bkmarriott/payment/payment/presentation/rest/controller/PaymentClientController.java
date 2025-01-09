package com.bkmarriott.payment.payment.presentation.rest.controller;

import static com.bkmarriott.payment.payment.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.payment.payment.application.service.PaymentService;
import com.bkmarriott.payment.payment.domain.Payment;
import com.bkmarriott.payment.payment.presentation.rest.dto.CreatePayment;
import com.bkmarriott.payment.payment.presentation.rest.dto.UpdatePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/internal")
public class PaymentClientController {

  private final PaymentService paymentService;

  @PostMapping
  public Long createPayment(
      @RequestBody CreatePayment.Requset request,
      @RequestHeader(HEADER_USER_ID) String userId) {
    log.info("[PaymentClientController] [createPayment] userId: {}", userId);
    CreatePayment.Response response = CreatePayment.Response.from(
        paymentService.createPayment(request.toDomain()));
    return response.getId();
  }

  @PatchMapping
  public void updateReservationId(
      @RequestBody UpdatePayment updatePayment,
      @RequestHeader(HEADER_USER_ID) String userId) {
    log.info("[PaymentClientController] [updateReservationId] userId: {}", userId);
    Payment payment = paymentService.updateReservationId(updatePayment.getPaymentId(), updatePayment.getReservationId());
  }
}
