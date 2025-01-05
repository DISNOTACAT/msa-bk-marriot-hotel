package com.bkmarriott.reservationservice.reservation.presentation.rest.exception;

import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Error> exceptionHandle(ResourceNotFoundException exception) {
    log.error("ResourceNotFoundException Handle={}", exception);
    return ApiResponse.error(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> exceptionHandle(Exception exception) {
    log.error("ExceptionHadnle={}", exception);
    return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
  }
}
