package com.bkmarriott.reservationservice.reservation.application.exception;

public class InventoryUpdateFailureException extends RuntimeException {

    public InventoryUpdateFailureException(String message) {
        super(message);
    }
}
