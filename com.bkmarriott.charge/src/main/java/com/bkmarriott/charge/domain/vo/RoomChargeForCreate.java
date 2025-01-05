package com.bkmarriott.charge.domain.vo;

import java.time.LocalDate;

public record RoomChargeForCreate(RoomChargeId id, Integer charge) {

    public static RoomChargeForCreate of(Long hotelId, RoomType roomType, LocalDate date, Integer charge) {
        return new RoomChargeForCreate(RoomChargeId.of(hotelId, roomType, date), charge);
    }
}
