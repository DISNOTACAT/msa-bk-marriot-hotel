package com.bkmarriott.reservationservice.reservation.application.outputport.feign;

import java.time.LocalDate;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;


public interface ChargeOutputPort {
    int getRoomCharge(Long hotelId, RoomType roomType, LocalDate startDate, LocalDate endDate);
}
