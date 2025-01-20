package com.bkmarriott.hotel.infrastructure.feignClient.adapter;

import com.bkmarriott.hotel.application.outputport.ChargeOutputPort;
import com.bkmarriott.hotel.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargeFeignClientAdapter implements ChargeOutputPort {

    private final ChargeClient chargeClient;

    @CircuitBreaker(name ="hotel-service", fallbackMethod = "fallbackGetRoomCharge")
    @Override
    public List<RoomChargeResponse> getRoomCharge(List<Long> hotelIds, LocalDate date) {

        return chargeClient.getRoomCharge(hotelIds, RoomType.STANDARD, date);
    }

    public List<RoomChargeResponse> fallbackGetRoomCharge(List<Long> hotelIds, LocalDate date, Throwable throwable){
        log.error("[ChargeFeignClientAdapter] [fallbackGetRoomCharge] ErrorMessage ::: {} , ... ", throwable.getMessage());
        return List.of();
    }

}
