package com.bkmarriott.hotel.domain;

import lombok.Getter;

@Getter
public class Hotel {
    private Long hotelId;
    private String name;
    private String country;
    private String city;
    private String address;
    private String description;

    public Hotel(Long hotelId, String name, String country, String city, String address, String description) {
        this.hotelId = hotelId;
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.description = description;
    }
}
