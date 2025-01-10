package com.bkmarriott.payment.presentation.rest.exception;

public class InvalidAccessException extends RuntimeException {

    public InvalidAccessException(String message) {
        super(message);
    }
}
