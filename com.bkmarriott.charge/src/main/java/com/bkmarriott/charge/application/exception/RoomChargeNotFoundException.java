package com.bkmarriott.charge.application.exception;

import static com.bkmarriott.charge.application.exception.RoomChargeErrorMessage.ROOM_CHARGE_NOT_EXIST;

public class RoomChargeNotFoundException extends RuntimeException {

    public RoomChargeNotFoundException() {
        super(ROOM_CHARGE_NOT_EXIST.getMessage());
    }
}