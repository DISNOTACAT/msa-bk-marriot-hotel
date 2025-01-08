package com.bkmarriott.reservationservice.reservation.presentation.rest.exception;

public class AuthAuditingException extends RuntimeException {

    public AuthAuditingException(String message) {
        super(message);
    }
}
