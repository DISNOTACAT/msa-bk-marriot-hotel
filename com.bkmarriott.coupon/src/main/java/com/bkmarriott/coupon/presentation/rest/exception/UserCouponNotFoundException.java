package com.bkmarriott.coupon.presentation.rest.exception;

public class UserCouponNotFoundException extends DefaultException {
    public UserCouponNotFoundException() {
        super(ExceptionStatus.USER_COUPON_NOT_FOUND);
    }
}
