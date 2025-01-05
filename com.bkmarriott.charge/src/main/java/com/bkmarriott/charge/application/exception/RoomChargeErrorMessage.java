package com.bkmarriott.charge.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomChargeErrorMessage {
    ROOM_TYPE_DUPLICATED("중복된 객실 타입입니다."),
    ROOM_CHARGE_NOT_EXIST("객실 요금이 존재하지 않습니다.");

    private final String message;
}
