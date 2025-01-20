package com.bkmarriott.hotel.infrastructure.feignClient.client;

import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name="charge-service")
public interface ChargeClient {

    @GetMapping("/api/v1/charges/hotels")
    List<RoomChargeResponse> getRoomCharge(@RequestParam List<Long> hotelIds, @RequestParam RoomType roomType, @RequestParam LocalDate date);
}
