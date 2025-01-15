package com.bkmarriott.charge.presentation.rest.dto;

import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class FindRoomChargeByHotelIds {

    @Getter
    @AllArgsConstructor
    public static class Request {

        private List<Long> hotelIds;
        private RoomType roomType;
        private LocalDate date;

        public List<RoomChargeId> toDomain() {
            return hotelIds.stream().map(hotelId -> RoomChargeId.of(hotelId, roomType, date)).toList();
        }
    }
}
