package com.bkmarriott.coupon.application.service;

import static org.assertj.core.api.Assertions.*;

import com.bkmarriott.coupon.application.outputport.CouponOutputPort;
import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.CouponPolicy;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] [Unit] CouponService Test")
class CouponServiceTest {

    private static Coupon TEST_COUPON;

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponOutputPort couponOutputPort;

    @BeforeAll
    static void beforeAll() {
        CouponPolicy couponPolicy = new CouponPolicy(
            1L, CouponPolicyType.FIXED, null, LocalDateTime.MIN, LocalDateTime.MAX
        );
        TEST_COUPON = new Coupon(1L, couponPolicy, "test", 10.0f);
    }

    @Test
    @DisplayName("[성공] 쿠폰 조회 테스트 - 유효한 식별자가 주어진 경우 쿠폰 도메인 반환")
    void findCoupon_successTest() {
        // Given
        Mockito.when(couponOutputPort.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(TEST_COUPON));
        // When
        Coupon coupon = couponService.findCoupon(TEST_COUPON.getId());
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(TEST_COUPON.getId(), coupon.getId()),
            () -> Assertions.assertEquals(TEST_COUPON.getName(), coupon.getName()),
            () -> Assertions.assertEquals(TEST_COUPON.getCouponPolicy(), coupon.getCouponPolicy()),
            () -> Assertions.assertEquals(TEST_COUPON.getDiscountRate(), coupon.getDiscountRate())
        );
    }

    @Test
    @DisplayName("[실패] 쿠폰 조회 테스트 - 유효하지 않은 식별자가 주어진 경우 예외 발생")
    void findCoupon_failureTest() {
        // Given
        Mockito.when(couponOutputPort.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() -> couponService.findCoupon(TEST_COUPON.getId()))
                .isInstanceOf(RuntimeException.class)
        );
    }
}