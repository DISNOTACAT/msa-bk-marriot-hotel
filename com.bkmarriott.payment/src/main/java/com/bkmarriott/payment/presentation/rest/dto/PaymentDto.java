package com.bkmarriott.payment.presentation.rest.dto;

import com.bkmarriott.payment.domain.Payment;

public record PaymentDto(
        Long paymentId,
        Long reservationId,
        Long originalPrice,
        Long finalPrice,
        String paymentType,
        String transactionalId,
        Long appliedCoupon
) {
    public static PaymentDto from(Payment payment){
        return new PaymentDto(
                payment.getId(),
            payment.getReservationId(),
            payment.getOriginalPrice(),
            payment.getFinalPrice(),
            payment.getPaymentType().toString(),
            payment.getTransactionId(),
            payment.getMemberCouponId()
        );
    }
}
