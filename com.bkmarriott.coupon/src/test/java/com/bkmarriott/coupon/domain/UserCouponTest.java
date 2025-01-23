package com.bkmarriott.coupon.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.domain.couponpolicy.CouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.LegacyCouponPolicy;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] UserCoupon Test")
class UserCouponTest {

    private static Coupon testCoupon;

    @BeforeAll
    static void beforeAll() {
        CouponPolicy couponPolicy = new LegacyCouponPolicy(
            1L, CouponPolicyType.FIXED, null, LocalDateTime.MIN, LocalDateTime.MAX
        );
        testCoupon = new Coupon(1L, couponPolicy, "test", 10.0f);
    }

    @Test
    @DisplayName("[성공] 정적 팩토리 메소드 테스트 - 생성 후 객체를 반환한다.")
    void staticConstructor_successTest() {
        // Given
        LocalDateTime issuedAt = LocalDateTime.now();
        // When
        UserCoupon userCoupon = UserCoupon.generateWithoutIdAndSpentAt(
            testCoupon, 1L, issuedAt, testCoupon.calcCouponExpireTime(issuedAt)
        );
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(issuedAt, userCoupon.getIssuedAt()),
            () -> Assertions.assertEquals(testCoupon, userCoupon.getCoupon()),
            () -> Assertions.assertEquals(testCoupon.calcCouponExpireTime(issuedAt), userCoupon.getExpiredAt()),
            () -> Assertions.assertEquals(1L, userCoupon.getUserId())
        );
    }
}