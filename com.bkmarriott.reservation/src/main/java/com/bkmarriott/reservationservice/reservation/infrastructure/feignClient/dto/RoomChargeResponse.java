package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;

public record RoomChargeResponse(
        Long hotelId,
        RoomType roomType,
        int charge,
        LocalDate date
) {
}
