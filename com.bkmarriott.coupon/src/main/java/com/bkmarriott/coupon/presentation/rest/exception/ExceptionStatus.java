package com.bkmarriott.coupon.presentation.rest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {

    USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "C_001", "해당하는 User Coupon을 찾을 수 없습니다."),
    USER_COUPON_NOT_SPENT(HttpStatus.BAD_REQUEST, "C_002", "아직 사용 전인 쿠폰입니다.");

    private final int status;
    private final String customCode;
    private final String message;
    private final String err;

    ExceptionStatus(HttpStatus httpStatus, String customCode, String message) {
        this.status = httpStatus.value();
        this.customCode = customCode;
        this.message = message;
        this.err = httpStatus.getReasonPhrase();
    }
}
