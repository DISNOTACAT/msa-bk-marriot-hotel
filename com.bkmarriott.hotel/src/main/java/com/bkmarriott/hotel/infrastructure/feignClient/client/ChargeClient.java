package com.bkmarriott.hotel.infrastructure.feignClient.client;

import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name="charge-service")
public interface ChargeClient {

    @GetMapping("/api/v1/charges")
    RoomChargeResponse getRoomCharge(@RequestParam Long hotelId, @RequestParam RoomType roomType, @RequestParam LocalDate date);
}
