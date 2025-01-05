package com.bkmarriott.hotel.presentation.rest.dto.response;


import com.bkmarriott.hotel.application.dto.HotelSearchResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record HotelSearchResponse(
        Long hotelId,
        String name,
        String city,
        String country,
        String address,
        String description,
        int charge
) {
    public static HotelSearchResponse toDto(HotelSearchResponseDto dto) {
        return new HotelSearchResponse(
                dto.getHotelId(),
                dto.getName(),
                dto.getCity(),
                dto.getCountry(),
                dto.getAddress(),
                dto.getDescription(),
                dto.getCharge()
        );
    }
}
