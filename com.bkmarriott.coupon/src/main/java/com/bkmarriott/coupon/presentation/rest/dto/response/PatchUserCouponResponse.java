package com.bkmarriott.coupon.presentation.rest.dto.response;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.UserCoupon;
import java.time.LocalDateTime;

public record PatchUserCouponResponse(
        Long id,
        Coupon coupon,
        Long userId,
        LocalDateTime issuedAt,
        LocalDateTime spentAt,
        LocalDateTime expiredAt
) {
    public static PatchUserCouponResponse from(UserCoupon userCoupon) {
        return new PatchUserCouponResponse(
                userCoupon.getId(),
                userCoupon.getCoupon(),
                userCoupon.getUserId(),
                userCoupon.getIssuedAt(),
                userCoupon.getSpentAt(),
                userCoupon.getExpiredAt()
        );
    }
}
