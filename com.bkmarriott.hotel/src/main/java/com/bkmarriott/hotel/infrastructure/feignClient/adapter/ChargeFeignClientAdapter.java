package com.bkmarriott.hotel.infrastructure.feignClient.adapter;

import com.bkmarriott.hotel.application.outputport.ChargeOutputPort;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargeFeignClientAdapter implements ChargeOutputPort {

    private final ChargeClient chargeClient;

    @CircuitBreaker(name ="hotel-service", fallbackMethod = "fallbackGetRoomCharge")
    @Override
    public int getRoomCharge(Hotel hotel, LocalDate date) {

        RoomChargeResponse roomCharge = chargeClient.getRoomCharge(hotel.getHotelId(), RoomType.STANDARD, date);

        return roomCharge.charge();
    }

    public int fallbackGetRoomCharge(Hotel hotel, LocalDate date, Throwable throwable){
        log.error("[ChargeFeignClientAdapter] [fallbackGetRoomCharge] ErrorMessage ::: {} , ... ", throwable.getMessage());
        return -1;
    }

}
