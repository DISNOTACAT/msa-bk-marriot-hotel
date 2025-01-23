package com.bkmarriott.reservationservice.reservation.infrastructure.cache.util;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;

import java.time.LocalDate;

public class RedisKeyFactory {
    public static final String LOCK_KEY = "inventory:lock";
    public static final String SEQUENCE_KEY = "inventory:sequenceNumber";
    private static final String KEY_FORMAT = "inventory:%d:%s:%s";

    public static String generateInventoryKey(Long hotelId, LocalDate date, RoomType roomType){
        return String.format(KEY_FORMAT, hotelId, date.toString(), roomType.name());
    }

}
