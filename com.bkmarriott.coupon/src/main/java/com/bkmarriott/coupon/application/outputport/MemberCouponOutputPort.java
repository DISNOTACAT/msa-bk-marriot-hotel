package com.bkmarriott.coupon.application.outputport;

import com.bkmarriott.coupon.domain.MemberCoupon;

public interface MemberCouponOutputPort {
    MemberCoupon getById(Long id);
}
