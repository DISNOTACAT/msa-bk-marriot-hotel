package com.bkmarriott.hotel.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "m_hotel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HotelEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition="TEXT")
    private String description;

}

