package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.RoomChargeResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="charge-service")
public interface ChargeClient {

    @GetMapping("/api/v1/charges/dates")
    List<RoomChargeResponse> findRoomChargeByDates(
        @RequestParam Long hotelId, @RequestParam RoomType roomType,
        @RequestParam LocalDate startDate, @RequestParam LocalDate endDate);
}
