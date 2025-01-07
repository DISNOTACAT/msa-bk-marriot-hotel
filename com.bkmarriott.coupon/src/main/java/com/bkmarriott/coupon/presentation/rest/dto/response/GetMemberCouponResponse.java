package com.bkmarriott.coupon.presentation.rest.dto.response;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.MemberCoupon;
import java.time.LocalDateTime;

public record GetMemberCouponResponse(
        Long id,
        Coupon coupon,
        Long memberId,
        LocalDateTime issuanceAt,
        LocalDateTime spendingAt,
        LocalDateTime expiredAt
) {
    public static GetMemberCouponResponse from(MemberCoupon memberCoupon) {
        return new GetMemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCoupon(),
                memberCoupon.getMemberId(),
                memberCoupon.getIssuanceAt(),
                memberCoupon.getSpendingAt(),
                memberCoupon.getExpiredAt()
        );
    }
}
