package com.bkmarriott.coupon.domain.vo;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.UserCoupon;
import java.time.LocalDateTime;

public record UserCouponForIssue(Long userId, Long couponId, LocalDateTime issuedAt) {

    public UserCoupon toUserCoupon(Coupon coupon) {
        LocalDateTime expireTime = coupon.calcCouponExpireTime(issuedAt);
        return UserCoupon.generateWithoutIdAndSpentAt(coupon, userId, issuedAt, expireTime);
    }
}
