package com.bkmarriott.payment.payment.presentation.rest.controller;

import static com.bkmarriott.payment.payment.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.payment.payment.application.service.PaymentService;
import com.bkmarriott.payment.payment.presentation.rest.dto.CreatePayment;
import com.bkmarriott.payment.payment.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.payment.payment.presentation.rest.util.reponse.ApiResponse.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentCommandController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<Success<CreatePayment.Response>> createPayment(
      @RequestBody CreatePayment.Requset request,
      @RequestHeader(HEADER_USER_ID) String userId) {

    CreatePayment.Response response = CreatePayment.Response.from(
        paymentService.createPayment(request.toDomain()));

    return ApiResponse.success(response, HttpStatus.CREATED);
  }

}
