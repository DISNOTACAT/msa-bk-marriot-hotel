package com.bkmarriott.reservationservice.reservation.domain.vo;

import com.bkmarriott.reservationservice.reservation.application.dto.InventorySearchRequestDto;
import java.time.LocalDate;

public record InventoryDateQuery(
        Long hotelId,
        LocalDate startDate,
        LocalDate endDate
) {
    public static InventoryDateQuery fromSearchDto(InventorySearchRequestDto searchDto) {
        return new InventoryDateQuery(searchDto.getHotelId(), searchDto.getStartDate(), searchDto.getEndDate());
    }
}
