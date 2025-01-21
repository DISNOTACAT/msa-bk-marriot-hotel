package com.bkmarriott.coupon.presentation.rest.controller;

import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import java.util.ArrayList;
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

@DisplayName("[Presentation] [Unit] CouponQueryController Test")
@WebMvcTest(CouponQueryController.class)
public class CouponQueryControllerTest {
    @MockitoBean
    private UserCouponQueryAdapter userCouponQueryAdapter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[성공] 유저 쿠폰 검증 테스트 - 쿠폰이 사용 가능한지 검증 후 쿠폰 정보 반환")
    void getUserCoupon_successTest_couponIsAvailable() throws Exception {
        // Given
        String requestUrl = "/api/v1/coupons/user-coupons/1";

        Mockito.when(userCouponQueryAdapter.getById(ArgumentMatchers.anyLong()))
                .thenReturn(Mockito.mock(UserCoupon.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 1L)
                        .header("X-Role", "CUSTOMER"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[성공] 유저별 보유 쿠폰 목록 조회 테스트 - 유저 아이디가 일치하는 쿠폰 정보 목록 반환")
    void getUserCouponList_successTest() throws Exception {
        // Given
        String requestUrl = "/api/v1/coupons/user-coupons";

        List<UserCoupon> userCouponList = new ArrayList<>();
        userCouponList.add(Mockito.mock(UserCoupon.class));

        Mockito.when(userCouponQueryAdapter.getUserCouponListByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(userCouponList);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 1L)
                        .header("X-Role", "CUSTOMER"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
