package com.bkmarriott.charge.domain;

import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultRoomCharge {

    private Long id;
    private RoomType roomType;
    private Integer charge;

    public DefaultRoomCharge(RoomType roomType, Integer charge) {
        this.roomType = roomType;
        this.charge = charge;
    }
}
