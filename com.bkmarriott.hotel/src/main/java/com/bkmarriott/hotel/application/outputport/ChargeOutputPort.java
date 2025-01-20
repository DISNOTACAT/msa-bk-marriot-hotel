package com.bkmarriott.hotel.application.outputport;

import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChargeOutputPort {
    List<RoomChargeResponse> getRoomCharge(List<Long> hotelIds, LocalDate date);
}
