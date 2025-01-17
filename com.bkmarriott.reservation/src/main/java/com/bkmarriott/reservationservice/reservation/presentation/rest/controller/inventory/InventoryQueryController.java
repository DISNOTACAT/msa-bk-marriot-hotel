package com.bkmarriott.reservationservice.reservation.presentation.rest.controller.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.InventorySearchRequestDto;
import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryQueryService;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response.InventorySearchResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.reponse.ApiResponse.Success;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations/inventories")
public class InventoryQueryController {

  private final InventoryQueryService inventoryService;

  @GetMapping
  public ResponseEntity<Success<List<InventorySearchResponse>>> getInventoryQuantity(
      @RequestParam Long hotelId,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {

    List<InventorySearchResponse> responseList = inventoryService.getAvailableRoomsWithCharge(
        InventorySearchRequestDto.builder()
            .hotelId(hotelId)
            .startDate(startDate)
            .endDate(endDate)
            .build());

    return ApiResponse.success(responseList, HttpStatus.OK);
  }
}
