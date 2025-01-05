package com.bkmarriott.reservationservice.reservation.presentation.rest.exception;

public class InvalidAccessException extends RuntimeException {

    public InvalidAccessException(String message) {
        super(message);
    }
}
