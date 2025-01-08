package com.bkmarriott.reservationservice.reservation.application.exception.reservation;

public class NoRoomAvailable extends RuntimeException {

    public NoRoomAvailable(String message) {
        super(message);
    }
}
