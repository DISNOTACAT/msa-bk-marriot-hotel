package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.RoomChargeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargeFeignClientAdapter implements ChargeOutputPort {

    private final ChargeClient chargeClient;

    @Override
    public int findRoomChargeByDates(InventoryQuery query) {

        return chargeClient.findRoomChargeByDates(query.hotelId(), query.roomType(), query.startDate(), query.endDate())
            .stream()
            .mapToInt(RoomChargeResponse::charge)
            .sum();
    }

}
