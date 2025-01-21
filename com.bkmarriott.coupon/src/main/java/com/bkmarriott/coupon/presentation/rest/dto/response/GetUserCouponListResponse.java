package com.bkmarriott.coupon.presentation.rest.dto.response;

import com.bkmarriott.coupon.domain.UserCoupon;
import org.springframework.data.domain.Page;

public record GetUserCouponListResponse(
        Page<UserCoupon> userCouponList
) {
    public static GetUserCouponListResponse from(Page<UserCoupon> userCoupons) {
        return new GetUserCouponListResponse(userCoupons);
    }
}
