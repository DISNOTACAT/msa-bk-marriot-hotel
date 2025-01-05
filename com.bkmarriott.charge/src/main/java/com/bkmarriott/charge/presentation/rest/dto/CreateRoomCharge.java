package com.bkmarriott.charge.presentation.rest.dto;

import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

public class CreateRoomCharge {

    @Getter
    @AllArgsConstructor
    public static class Request {

        private Long hotelId;
        private RoomType roomType;
        private LocalDate date;
        private Integer charge;

        public RoomChargeForCreate toDomain() {
            return RoomChargeForCreate.of(hotelId, roomType, date, charge);
        }
    }
}
