package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentDto;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="payment-service")
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    PaymentDto processPayment(@RequestBody PaymentRequestDto dto, @RequestHeader(name = "X-User-Id") Long userId, @RequestHeader(name = "X-Role") String role);

    @RequestMapping(value = "/api/v1/payments/refund/{paymentId}", method = RequestMethod.PATCH)
    PaymentDto processRefund(@PathVariable Long paymentId, @RequestHeader(name = "X-User-Id") Long userId, @RequestHeader(name = "X-Role") String role);

}
