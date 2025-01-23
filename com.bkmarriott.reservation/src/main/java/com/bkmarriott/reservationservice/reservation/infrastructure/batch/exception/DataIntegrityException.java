package com.bkmarriott.reservationservice.reservation.infrastructure.batch.exception;

public class DataIntegrityException extends RuntimeException {

    public DataIntegrityException(String message) {
        super(message);
    }
}
