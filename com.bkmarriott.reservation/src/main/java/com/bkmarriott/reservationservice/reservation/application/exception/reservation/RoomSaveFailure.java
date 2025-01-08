package com.bkmarriott.reservationservice.reservation.application.exception.reservation;

public class RoomSaveFailure extends RuntimeException {

    public RoomSaveFailure(String message) {
        super(message);
    }
}
