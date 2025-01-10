package com.bkmarriott.payment.application.dto;

import com.bkmarriott.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.domain.vo.PaymentType;
import com.bkmarriott.payment.presentation.rest.dto.PaymentRequestDto;


public record CreatePaymentDto (

  Long reservationId,
  String cardNumber,
  String expiryDate,
  String cvv,
  Long appliedCoupon,
  Long originalPrice,
  Long finalPrice,
  PaymentStatus paymentStatus,
  PaymentType paymentType
) {
  public static CreatePaymentDto from(PaymentRequestDto paymentRequest) {
    return new CreatePaymentDto(
        paymentRequest.reservationId(),
        paymentRequest.cardNumber(),
        paymentRequest.expiryDate(),
        paymentRequest.cvv(),
        paymentRequest.appliedCoupon(),
        paymentRequest.originalPrice(),
        paymentRequest.finalPrice(),
        PaymentStatus.PENDING,
        PaymentType.valueOf(paymentRequest.method())
    );
  }
}
