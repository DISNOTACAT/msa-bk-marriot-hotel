package com.bkmarriott.coupon.application.outputport;

import com.bkmarriott.coupon.domain.Coupon;
import java.util.Optional;

public interface CouponOutputPort {

    Optional<Coupon> findById(Long couponId);
}
