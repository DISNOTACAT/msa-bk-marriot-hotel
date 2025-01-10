package com.bkmarriott.payment.application.exception;

public class SavePaymentFailureException extends RuntimeException {

    public SavePaymentFailureException(String message) {
        super(message);
    }
}
