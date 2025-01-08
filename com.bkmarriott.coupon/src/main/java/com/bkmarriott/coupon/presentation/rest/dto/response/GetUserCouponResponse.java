package com.bkmarriott.coupon.presentation.rest.dto.response;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.UserCoupon;
import java.time.LocalDateTime;

public record GetUserCouponResponse(
        Long id,
        Coupon coupon,
        Long memberId,
        LocalDateTime issuanceAt,
        LocalDateTime spendingAt,
        LocalDateTime expiredAt
) {
    public static GetUserCouponResponse from(UserCoupon userCoupon) {
        return new GetUserCouponResponse(
                userCoupon.getId(),
                userCoupon.getCoupon(),
                userCoupon.getUserId(),
                userCoupon.getIssuedAt(),
                userCoupon.getSpentAt(),
                userCoupon.getExpiredAt()
        );
    }
}
