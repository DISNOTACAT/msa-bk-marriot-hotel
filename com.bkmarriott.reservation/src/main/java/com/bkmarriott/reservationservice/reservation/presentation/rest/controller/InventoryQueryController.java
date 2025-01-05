package com.bkmarriott.reservationservice.reservation.presentation.rest.controller;

import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Request;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Response;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Success;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations/inventories")
public class InventoryQueryController {

  private final InventoryService inventoryService;

  @GetMapping
  public ResponseEntity<Success<List<Response>>> getInventoryQuantity(@RequestBody Request request) {

    List<Response> responseList = inventoryService.getInventoryQuantity(request);

    return ApiResponse.success(responseList, HttpStatus.OK);
  }
}
