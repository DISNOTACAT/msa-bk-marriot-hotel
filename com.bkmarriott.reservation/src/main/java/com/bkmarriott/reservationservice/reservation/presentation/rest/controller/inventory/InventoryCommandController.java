package com.bkmarriott.reservationservice.reservation.presentation.rest.controller.inventory;

import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryService;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response.InventoryModificationResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Success;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations/inventories")
public class InventoryCommandController {

  private final InventoryService inventoryService;

  @PatchMapping
  public ResponseEntity<Success<List<InventoryModificationResponse>>> updateInventory(@RequestParam Long reservationId) {

    List<InventoryModificationResponse> responseList = inventoryService.updateTotalReservedInventory(reservationId)
        .stream().map(InventoryModificationResponse::from).toList();

    return ApiResponse.success(responseList, HttpStatus.OK);
  }
}
