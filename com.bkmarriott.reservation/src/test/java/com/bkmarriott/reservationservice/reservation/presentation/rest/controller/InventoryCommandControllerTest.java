package com.bkmarriott.reservationservice.reservation.presentation.rest.controller;

import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(InventoryCommandController.class)
@DisplayName("[Presentation] InventoryCommandController Unit Test")
class InventoryCommandControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private InventoryService inventoryService;

  @Test
  @DisplayName("[인벤토리 업데이트 성공 테스트] 예약 아이디가 주어질 경우, 인벤토리 예약 객실 수를 증가 시킨다.")
  void update_successTest() throws Exception {
    //Given
    Long reservationId = 1L;

    String requestUrl = "/api/v1/reservations/inventories?reservationId=" + reservationId;

    Inventory increase = new Inventory(
        101L,
        LocalDate.of(2025, 2, 1),
        RoomType.DELUXE,
        80,
        79
    );

    Mockito.when(inventoryService.updateTotalReserved(reservationId))
            .thenReturn(List.of(increase));

    //When
    mockMvc.perform(MockMvcRequestBuilders.patch(requestUrl)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hotelId").value(101))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].date").value(LocalDate.of(2025, 2, 1).toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roomType").value(RoomType.DELUXE.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].totalInventory").value(80))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].totalReserved").value(79));
  }

}
