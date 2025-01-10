package com.bkmarriott.charge.domain;

import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotelType {

    private Long id;
    private Long hotelId;
    private RoomType roomType;

    public HotelType(Long hotelId, RoomType roomType) {
        this.hotelId = hotelId;
        this.roomType = roomType;
    }
}
