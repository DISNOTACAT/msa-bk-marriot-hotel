package com.bkmarriott.coupon.presentation.rest.exception;

public class UserIdMissingException extends DefaultException {

    public UserIdMissingException() {
        super(ExceptionStatus.USER_ID_MISSING);
    }
}
