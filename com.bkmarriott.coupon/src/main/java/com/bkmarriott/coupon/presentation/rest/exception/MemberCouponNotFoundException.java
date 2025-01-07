package com.bkmarriott.coupon.presentation.rest.exception;

public class MemberCouponNotFoundException extends DefaultException {
    public MemberCouponNotFoundException() {
        super(ExceptionStatus.MEMBER_COUPON_NOT_FOUND);
    }
}
