package com.bkmarriott.hotel.domain;

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

    public Long getHotelId() { return hotelId; }

    public String getName() { return name; }

    public String getCountry() { return country; }

    public String getCity() { return city; }

    public String getAddress() { return address; }

    public String getDescription() { return description; }
}
