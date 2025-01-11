package com.bkmarriott.coupon.infrastructure.persistence.exception;

import com.bkmarriott.coupon.presentation.rest.exception.DefaultException;
import com.bkmarriott.coupon.presentation.rest.exception.ExceptionStatus;

public class CouponNotSpentException extends DefaultException {
    public CouponNotSpentException() {
        super(ExceptionStatus.USER_COUPON_NOT_SPENT);
    }
}
