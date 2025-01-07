package com.bkmarriott.promotion.presentation.rest.exception;

import com.bkmarriott.promotion.presentation.rest.exception.exceptions.InvalidAccessException;
import com.bkmarriott.promotion.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.promotion.presentation.rest.util.reponse.ApiResponse.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(InvalidAccessException.class)
    public ResponseEntity<Error> invalidAccessHandle(InvalidAccessException exception) {
        log.error("[GlobalExceptionControllerAdvice] [invalidAccessHandler] exception ::: ", exception);
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> exceptionHandle(Exception exception) {
        log.error("[GlobalExceptionControllerAdvice] [exceptionHandle] exception ::: ", exception);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
