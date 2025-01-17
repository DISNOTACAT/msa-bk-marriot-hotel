package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.ChargeClient;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargeFeignClientAdapter implements ChargeOutputPort {

    private final ChargeClient chargeClient;

    @Override
    public int getRoomCharge(InventoryQuery query) {

        return Stream.iterate(query.startDate(), date -> date.plusDays(1))
            .limit(query.startDate().until(query.endDate()).getDays() + 1)
            .mapToInt(date -> chargeClient.getRoomCharge(query.hotelId(), query.roomType(), date).charge())
            .sum();
    }

}
