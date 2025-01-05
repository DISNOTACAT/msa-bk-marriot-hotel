package com.bkmarriott.charge.application.exception;

import static com.bkmarriott.charge.application.exception.RoomChargeErrorMessage.ROOM_TYPE_DUPLICATED;

public class RoomChargeDuplicatedException extends RuntimeException {

    public RoomChargeDuplicatedException() {
        super(ROOM_TYPE_DUPLICATED.getMessage());
    }
}