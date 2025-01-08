package com.bkmarriott.coupon.presentation.rest.controller;

import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import com.bkmarriott.coupon.presentation.rest.dto.response.GetUserCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@RestController
public class CouponQueryController {

    private final UserCouponQueryAdapter userCouponQueryAdapter;

    @GetMapping("/user-coupons/{id}")
    public ResponseEntity<GetUserCouponResponse> getUserCoupon(@PathVariable Long id) {
        UserCoupon userCoupon = userCouponQueryAdapter.getById(id);
        return ResponseEntity.ok(GetUserCouponResponse.from(userCoupon));
    }
}
