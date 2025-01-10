package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.config.FeignOkHttpConfiguration;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="coupon-service", configuration = FeignOkHttpConfiguration.class)
public interface CouponClient {

    @GetMapping("/api/v1/coupons/user-coupons/{id}")
    CouponDto verifyCoupon(@PathVariable Long id);

    @RequestMapping(value = "/api/v1/coupons/user-coupons/{id}", method = RequestMethod.PATCH)
    CouponDto useCoupon(@PathVariable Long id);
}
