package com.bkmarriott.reservationservice.reservation.application.exception.inventory;

public class InventoryUpdateFailureException extends RuntimeException {

    public InventoryUpdateFailureException(String message) {
        super(message);
    }
}
