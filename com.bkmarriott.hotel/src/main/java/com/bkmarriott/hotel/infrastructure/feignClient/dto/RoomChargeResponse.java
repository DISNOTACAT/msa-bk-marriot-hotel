package com.bkmarriott.hotel.infrastructure.feignClient.dto;

import java.time.LocalDate;

public record RoomChargeResponse(
        Long hotelId,
        RoomType roomType,
        Integer charge,
        LocalDate date
) {
}
