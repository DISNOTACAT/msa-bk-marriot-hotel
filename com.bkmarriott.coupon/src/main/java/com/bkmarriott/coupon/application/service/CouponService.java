package com.bkmarriott.coupon.application.service;

import com.bkmarriott.coupon.application.outputport.CouponOutputPort;
import com.bkmarriott.coupon.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponOutputPort couponOutputPort;

    public Coupon findCoupon(Long couponId) {
        return couponOutputPort.findById(couponId)
            .orElseThrow(RuntimeException::new);
    }
}
