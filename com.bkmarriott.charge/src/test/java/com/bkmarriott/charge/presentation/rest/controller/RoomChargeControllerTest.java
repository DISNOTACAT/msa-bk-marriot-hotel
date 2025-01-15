package com.bkmarriott.charge.presentation.rest.controller;

import com.bkmarriott.charge.application.service.RoomChargeService;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import com.bkmarriott.charge.presentation.rest.dto.CreateRoomCharge;
import com.bkmarriott.charge.presentation.rest.dto.FindRoomChargeByDates;
import com.bkmarriott.charge.presentation.rest.dto.FindRoomChargeByHotelIds;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.List;

@DisplayName("[Presentation] RoomCharge Controller Unit Test")
@WebMvcTest(controllers = RoomChargeController.class)
class RoomChargeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomChargeService roomChargeService;

    @Test
    @DisplayName("[객실 요금 등록 성공 테스트] 객실 요금을 등록한다.")
    void createRoomCharge_successTest() throws Exception {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;

        String requestUrl = "/api/v1/charges";
        String requestBody = objectMapper.writeValueAsString(
                new CreateRoomCharge.Request(hotelId, roomType, date, charge)
        );

        RoomCharge mockRoomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);
        Mockito.when(roomChargeService.create(ArgumentMatchers.any(RoomChargeForCreate.class)))
                .thenReturn(mockRoomCharge);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotelId").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomType").value(roomType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.charge").value(charge));
    }

    @Test
    @DisplayName("[객실 요금 조회 성공 테스트] 호텔 ID, 객실 타입, 날짜가 일치하는 객실 요금을 반환한다.")
    void findRoomCharge_successTest() throws Exception {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;

        String requestUrl = String.format("/api/v1/charges?hotelId=%d&roomType=%s&date=%s",
                hotelId, roomType.name(), date);

        RoomCharge mockRoomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);
        Mockito.when(roomChargeService.findOne(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(mockRoomCharge);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotelId").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomType").value(roomType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.charge").value(charge));
    }

    @Test
    @DisplayName("[호텔 ID 목록으로 객실 요금 조회 성공 테스트] 호텔 ID 목록, 객실 타입, 날짜가 일치하는 객실 요금 목록을 반환한다.")
    void findRoomChargeByHotelIds_successTest() throws Exception {
        // Given
        List<Long> hotelIds = List.of(1L, 2L, 3L, 10L);
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;

        FindRoomChargeByHotelIds.Request request = new FindRoomChargeByHotelIds.Request(hotelIds, roomType, date);
        List<RoomCharge> mockRoomCharges = hotelIds.stream().map(
                hotelId -> new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge)
        ).toList();
        Mockito.when(roomChargeService.findAll(request.toDomain())).thenReturn(mockRoomCharges);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/charges/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("hotelIds", String.join(",", hotelIds.stream().map(Object::toString).toList()))
                        .param("roomType", roomType.name())
                        .param("date", date.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockRoomCharges.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hotelId").value(hotelIds.get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[-1].hotelId").value(hotelIds.get(hotelIds.size() - 1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomType").value(roomType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].charge").value(charge));
    }

    @Test
    @DisplayName("[시작 날짜와 끝 날짜로 객실 요금 조회 성공 테스트] 호텔 ID, 객실 타입, 시작 날짜부터 끝 날짜가 일치하는 객실 요금 목록을 반환한다.")
    void findRoomChargeByDates_successTest() throws Exception {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 3);

        Integer charge = 10000;

        FindRoomChargeByDates.Request request = new FindRoomChargeByDates.Request(hotelId, roomType, startDate, endDate);
        List<RoomCharge> mockRoomCharges = startDate.datesUntil(endDate.plusDays(1)).map(
                date -> new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge)
        ).toList();
        Mockito.when(roomChargeService.findAll(request.toDomain())).thenReturn(mockRoomCharges);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/charges/dates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("hotelId", hotelId.toString())
                        .param("roomType", roomType.name())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockRoomCharges.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hotelId").value(hotelId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomType").value(roomType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(startDate.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[-1].date").value(endDate.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].charge").value(charge));
    }

    @Test
    @DisplayName("[객실 요금 등록 수정 테스트] 객실 요금을 수정한다.")
    void updateRoomCharge_successTest() throws Exception {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;

        String requestUrl = "/api/v1/charges";
        String requestBody = objectMapper.writeValueAsString(
                new CreateRoomCharge.Request(hotelId, roomType, date, charge)
        );

        RoomCharge mockRoomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);
        Mockito.when(roomChargeService.update(ArgumentMatchers.any(RoomChargeForCreate.class)))
                .thenReturn(mockRoomCharge);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch(requestUrl)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotelId").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomType").value(roomType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.charge").value(charge));
    }
}