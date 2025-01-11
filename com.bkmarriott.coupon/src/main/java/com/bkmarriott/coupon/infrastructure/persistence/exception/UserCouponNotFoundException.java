package com.bkmarriott.coupon.infrastructure.persistence.exception;

import com.bkmarriott.coupon.presentation.rest.exception.DefaultException;
import com.bkmarriott.coupon.presentation.rest.exception.ExceptionStatus;

public class UserCouponNotFoundException extends DefaultException {
    public UserCouponNotFoundException() {
        super(ExceptionStatus.USER_COUPON_NOT_FOUND);
    }
}
