package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.ChargeClient;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargeFeignClientAdapter implements ChargeOutputPort {

    private final ChargeClient chargeClient;

    @Override
    public int getRoomCharge(Long hotelId, RoomType roomType, LocalDate startDate, LocalDate endDate) {

        return Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(startDate.until(endDate).getDays() + 1)
            .mapToInt(date -> chargeClient.getRoomCharge(hotelId, roomType, date).charge())
            .sum();
    }

}
