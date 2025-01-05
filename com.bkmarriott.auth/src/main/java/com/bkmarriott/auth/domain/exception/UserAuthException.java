package com.bkmarriott.auth.domain.exception;


public class UserAuthException extends RuntimeException {

    public UserAuthException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
