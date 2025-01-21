package com.bkmarriott.coupon.presentation.rest.exception;

public class UserRoleMissingException extends DefaultException {

    public UserRoleMissingException() {
        super(ExceptionStatus.USER_ROLE_MISSING);
    }
}
