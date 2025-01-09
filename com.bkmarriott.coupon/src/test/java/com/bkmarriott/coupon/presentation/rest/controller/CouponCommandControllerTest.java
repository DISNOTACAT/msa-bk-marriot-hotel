package com.bkmarriott.coupon.presentation.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.application.service.UserCouponService;
import com.bkmarriott.coupon.domain.UserCoupon;
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

@DisplayName("[Presentation] [Unit] CouponCommandController Test")
@WebMvcTest(CouponCommandController.class)
class CouponCommandControllerTest {

    @MockitoBean
    private UserCouponService userCouponService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[성공] 쿠폰 사용 테스트 - 쿠폰 사용 시도 후 쿠폰 정보를 반환")
    void timeAttackCouponIssuance_successTest_issuanceSuccess() throws Exception {
        // Given
        String requestUrl = "/api/v1/coupons/user-coupons/1";

        Mockito.when(userCouponService.useUserCoupon(ArgumentMatchers.anyLong()))
            .thenReturn(Mockito.mock(UserCoupon.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1L)
                .header("X-Role", "CUSTOMER"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}