package com.bkmarriott.charge.domain.vo;

import java.time.LocalDate;

public record RoomChargeId(Long hotelId, RoomType roomType, LocalDate date) {

    public static RoomChargeId of(Long hotelId, RoomType roomType, LocalDate date) {
        return new RoomChargeId(hotelId, roomType, date);
    }
}
