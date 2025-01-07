package com.bkmarriott.coupon.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName(("[Domain] [Unit] Coupon Test"))
class CouponTest {

    @Test
    @DisplayName("[성공] 쿠폰 만료일 계산 테스트 - 쿠폰 정책에 맞는 만료일을 반환한다.")
    void calcCouponExpireTime_successTest() {
        // Given
        LocalDateTime endedAt = LocalDateTime.MAX;
        CouponPolicy couponPolicy = new CouponPolicy(
            1L, CouponPolicyType.FIXED, null, LocalDateTime.MIN, endedAt
        );
        Coupon coupon = new Coupon(1L, couponPolicy, "test", 10.0f);

        LocalDateTime baseTime = LocalDateTime.now();
        // When
        LocalDateTime actual = coupon.calcCouponExpireTime(baseTime);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(endedAt, actual)
        );
    }
}