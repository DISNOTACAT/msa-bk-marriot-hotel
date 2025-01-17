package com.bkmarriott.reservationservice.reservation.presentation.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.bkmarriott.reservationservice.reservation.application.dto.InventorySearchRequestDto;
import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryQueryService;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.presentation.rest.controller.inventory.InventoryQueryController;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response.InventorySearchResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(InventoryQueryController.class)
@DisplayName("[Presentation] InventoryQueryController Unit Test")
class InventoryQueryControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private InventoryQueryService inventoryService;

  @Test
  @DisplayName("[인벤토리 예약 가능 객실 수 조회 성공 테스트] 호텔 아이디와 숙박 일자가 주어졌을 시, 예약 가능한 타입별 객실 수를 반환한다.")
  void get_enable_inventory_quantity_SuccessTest() throws Exception {

    Long hotelId = 1L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);

    String requestUrl = "/api/v1/reservations/inventories";


    List<InventorySearchResponse> mockResponse = List.of(
        new InventorySearchResponse(RoomType.DELUXE, 2, 350000),
        new InventorySearchResponse(RoomType.STANDARD, 44, 220000),
        new InventorySearchResponse(RoomType.TWIN, 33, 300000)
    );
    Mockito.when(inventoryService.getAvailableRoomsWithCharge(ArgumentMatchers.any(InventorySearchRequestDto.class)))
        .thenReturn(mockResponse);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
            .param("hotelId", hotelId.toString())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roomType").value(RoomType.DELUXE.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roomCount").value(2))
        .andDo(print());
  }

}
