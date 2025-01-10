package com.bkmarriott.payment.presentation.rest.dto;

public record PaymentRequestDto(
        Long reservationId,
        String method,
        String cardNumber,
        String expiryDate,
        String cvv,
        Long appliedCoupon,
        Long originalPrice,
        Long finalPrice
) {
}
