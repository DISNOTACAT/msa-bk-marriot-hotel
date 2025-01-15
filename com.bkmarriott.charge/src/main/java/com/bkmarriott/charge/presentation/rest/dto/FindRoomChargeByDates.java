package com.bkmarriott.charge.presentation.rest.dto;

import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class FindRoomChargeByDates {

    @Getter
    @AllArgsConstructor
    public static class Request {

        private Long hotelId;
        private RoomType roomType;
        private LocalDate startDate;
        private LocalDate endDate;

        public List<RoomChargeId> toDomain() {
            return datesUntil().map(date -> RoomChargeId.of(hotelId, roomType, date)).toList();
        }

        private Stream<LocalDate> datesUntil() {
            return startDate.datesUntil(endDate.plusDays(1));
        }
    }
}
