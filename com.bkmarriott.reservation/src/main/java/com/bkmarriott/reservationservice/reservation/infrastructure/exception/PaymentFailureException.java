package com.bkmarriott.reservationservice.reservation.infrastructure.exception;

public class PaymentFailureException extends RuntimeException {

    public PaymentFailureException(String message) {
        super(message);
    }
}
