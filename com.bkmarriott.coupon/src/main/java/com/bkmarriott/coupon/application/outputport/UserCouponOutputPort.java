package com.bkmarriott.coupon.application.outputport;

import com.bkmarriott.coupon.domain.UserCoupon;

public interface UserCouponOutputPort {

    UserCoupon generateUserCoupon(UserCoupon userCoupon);
    UserCoupon findValidCouponById(Long userCouponId);
    UserCoupon update(UserCoupon userCoupon);
    UserCoupon cancelUserCouponUsage(UserCoupon userCoupon);
    UserCoupon findById(Long id);
}
