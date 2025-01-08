package com.bkmarriott.coupon.application.outputport;

import com.bkmarriott.coupon.domain.UserCoupon;

// SRP 책임 2개가 돼요
// Service - save
public interface UserCouponOutputPort {

    UserCoupon generateUserCoupon(UserCoupon userCoupon);
}
