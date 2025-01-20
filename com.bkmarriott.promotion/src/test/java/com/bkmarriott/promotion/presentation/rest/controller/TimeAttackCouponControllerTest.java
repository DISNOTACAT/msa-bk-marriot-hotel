package com.bkmarriott.promotion.presentation.rest.controller;

import com.bkmarriott.promotion.application.service.TimeAttackCouponIssuanceService;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import com.bkmarriott.promotion.presentation.rest.dto.TimeAttackCouponRequest;
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

@DisplayName("[Presentation] [Unit] TimeAttackCouponController Test")
@WebMvcTest(TimeAttackCouponController.class)
class TimeAttackCouponControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TimeAttackCouponIssuanceService timeAttackCouponIssuanceService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[성공] 선착순 쿠폰 발급 테스트 - 쿠폰 발급 성공 시 true를 반환")
    void timeAttackCouponIssuance_successTest_issuanceSuccess() throws Exception {
        // Given
        String requestUrl = "/api/v1/promotions/time-attack/issuance";
        String requestBody = objectMapper.writeValueAsString(
            new TimeAttackCouponRequest(1L)
        );

        Mockito.when(timeAttackCouponIssuanceService.issue(ArgumentMatchers.any(
            TimeAttackCouponIssuance.class
        ))).thenReturn(true);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1L)
                .header("X-Role", "CUSTOMER"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(String.valueOf(true)));
    }

    @Test
    @DisplayName("[성공] 선착순 쿠폰 발급 테스트 - 쿠폰 발급 실패 시 false를 반환")
    void timeAttackCouponIssuance_successTest_issuanceFailure() throws Exception {
        // Given
        String requestUrl = "/api/v1/promotions/time-attack/issuance";
        String requestBody = objectMapper.writeValueAsString(
            new TimeAttackCouponRequest(1L)
        );

        Mockito.when(timeAttackCouponIssuanceService.issue(ArgumentMatchers.any(
            TimeAttackCouponIssuance.class
        ))).thenReturn(false);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1L)
                .header("X-Role", "CUSTOMER"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(String.valueOf(false)));
    }
}