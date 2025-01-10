package com.bkmarriott.payment.application.exception;

public class RefundPaymentFailureException extends RuntimeException {

    public RefundPaymentFailureException(String message) {
        super(message);
    }
}
