package com.bkmarriott.reservationservice.reservation.presentation.rest.controller.reservation;

import static com.bkmarriott.reservationservice.reservation.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.reservationservice.reservation.application.service.ReservationService;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.reservation.CreateReservation;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationCommandController {

  private final ReservationService reservationService;

  @PostMapping
  public ResponseEntity<Success<CreateReservation.Response>> createReservation(
      @RequestBody CreateReservation.Request request,
      @RequestHeader(HEADER_USER_ID) String userId) {

    CreateReservation.Response response = CreateReservation.Response.from(
        reservationService.createReservation(request.toDomain(userId)));

    return ApiResponse.success(response, HttpStatus.CREATED);
  }
}
