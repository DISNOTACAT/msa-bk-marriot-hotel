package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.coupon.GetMemberCouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="coupon-service")
public interface CouponClient {

  @GetMapping("/api/v1/coupons/user-coupons/{id}")
  ResponseEntity<GetMemberCouponResponse> getUserCoupon(@PathVariable Long id);

}
