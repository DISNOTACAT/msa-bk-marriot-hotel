package com.bkmarriott.reservationservice.reservation.presentation.rest.controller.inventory;

import static com.bkmarriott.reservationservice.reservation.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.command.InventoryModification.Response;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Success;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations/inventories")
public class InventoryCommandController {

  private final InventoryService inventoryService;

  @PatchMapping
  public ResponseEntity<Success<List<Response>>> updateInventory(
      @RequestParam Long reservationId,
      @RequestHeader(HEADER_USER_ID) String userId
  ) {

    List<Response> responseList = inventoryService.updateTotalReserved(reservationId)
        .stream().map(Response::from).toList();

    return ApiResponse.success(responseList, HttpStatus.OK);
  }
}
