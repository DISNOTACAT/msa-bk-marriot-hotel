package com.bkmarriott.coupon.presentation.rest.controller;

import com.bkmarriott.coupon.application.service.UserCouponService;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.presentation.rest.dto.response.PatchUserCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@RestController
public class CouponCommandController {

    private final UserCouponService userCouponService;

    @PatchMapping("/user-coupons/{id}")
    public ResponseEntity<PatchUserCouponResponse> useUserCoupon(@PathVariable Long id) {
        UserCoupon userCoupon = userCouponService.useUserCoupon(id);
        return ResponseEntity.ok(PatchUserCouponResponse.from(userCoupon));
    }
}
