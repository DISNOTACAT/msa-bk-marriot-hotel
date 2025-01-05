package com.bkmarriott.hotel.application.dto;

import com.bkmarriott.hotel.domain.Hotel;
import lombok.Getter;

@Getter
public class HotelSearchResponseDto{
        private Long hotelId;
        private String name;
        private String city;
        private String country;
        private String address;
        private String description;
        private int charge;

    public HotelSearchResponseDto(Hotel hotel, int roomCharge) {
        this.hotelId = hotel.getHotelId();
        this.name = hotel.getName();
        this.city = hotel.getCity();
        this.country = hotel.getCountry();
        this.address = hotel.getAddress();
        this.description = hotel.getDescription();
        charge = roomCharge;
    }
}
