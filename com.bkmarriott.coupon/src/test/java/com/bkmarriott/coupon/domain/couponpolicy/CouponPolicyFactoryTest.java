package com.bkmarriott.coupon.domain.couponpolicy;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] CouponPolicyFactory Test")
class CouponPolicyFactoryTest {

    @Test
    @DisplayName("[성공] CouponPolicy 생성 테스트 - FixedCouponPolicy 반환")
    public void generateCouponPolicy_successTest_FixedCouponPolicy() {
        // Given & When
        CouponPolicy couponPolicy = CouponPolicyFactory.generateCouponPolicy(
            CouponPolicyType.FIXED,
            1L, 10, LocalDateTime.MIN, LocalDateTime.MAX
        );
        // Then
        Assertions.assertAll(
            () -> assertInstanceOf(FixedCouponPolicy.class, couponPolicy)
        );
    }

    @Test
    @DisplayName("[성공] CouponPolicy 생성 테스트 - AfterCouponPolicy 반환")
    public void generateCouponPolicy_successTest_AfterCouponPolicy() {
        // Given & When
        CouponPolicy couponPolicy = CouponPolicyFactory.generateCouponPolicy(
            CouponPolicyType.AFTER,
            1L, 10, LocalDateTime.MIN, LocalDateTime.MAX
        );
        // Then
        Assertions.assertAll(
            () -> assertInstanceOf(AfterCouponPolicy.class, couponPolicy)
        );
    }

    @Test
    @DisplayName("[성공] CouponPolicy 생성 테스트 - MixedCouponPolicy 반환")
    public void generateCouponPolicy_successTest_MixedCouponPolicy() {
        // Given & When
        CouponPolicy couponPolicy = CouponPolicyFactory.generateCouponPolicy(
            CouponPolicyType.MIXED,
            1L, 10, LocalDateTime.MIN, LocalDateTime.MAX
        );
        // Then
        Assertions.assertAll(
            () -> assertInstanceOf(MixedCouponPolicy.class, couponPolicy)
        );
    }
}