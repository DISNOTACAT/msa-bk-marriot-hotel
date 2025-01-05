
package com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {

    public static ResponseEntity<Object> success(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).build();
    }

    public static <T> ResponseEntity<Success<T>> success(T data, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(Success.of(data));
    }

    public static ResponseEntity<Error> error(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
            .body(Error.of(message));
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Error {

        private String message;

        public static Error of(@NonNull String errorMessage) {
            return new Error(errorMessage);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Success<T> {

        private T data;

        public static <T> Success<T> of(@NonNull T data) {
            return new Success<>(data);
        }
    }
}
