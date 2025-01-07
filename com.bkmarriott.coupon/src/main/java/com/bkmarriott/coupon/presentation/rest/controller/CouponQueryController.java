package com.bkmarriott.coupon.presentation.rest.controller;

import com.bkmarriott.coupon.application.outputport.MemberCouponOutputPort;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.MemberCouponQueryAdapter;
import com.bkmarriott.coupon.presentation.rest.dto.response.GetMemberCouponResponse;
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
    private final MemberCouponOutputPort memberCouponOutputPort;

    @GetMapping("/member-coupons/{id}")
    public ResponseEntity<GetMemberCouponResponse> getMemberCoupon(@PathVariable Long id) {
        return ResponseEntity.ok(GetMemberCouponResponse.from(memberCouponOutputPort.getById(id)));
    }
}
