package com.bkmarriott.reservationservice.reservation.infrastructure.exception;

public class DisabledCouponException extends RuntimeException {

    public DisabledCouponException(String message) {
        super(message);
    }
}
