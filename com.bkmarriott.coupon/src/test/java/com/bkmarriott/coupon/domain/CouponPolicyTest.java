package com.bkmarriott.coupon.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.domain.couponpolicy.AfterCouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.CouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.FixedCouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.LegacyCouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.MixedCouponPolicy;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] CouponPolicy Test")
class CouponPolicyTest {

    @Nested
    @DisplayName("[FixedCouponPolicy] 단위 테스트")
    class FixedCouponPolicyTest {

        @Test
        @DisplayName("[성공] 정책 만료일 계산 테스트 - 고정 만료일 반환")
        void calcExpireTime_successTest_forFixed() {
            // Given
            LocalDateTime endedAt = LocalDateTime.MAX;
            LocalDateTime baseTime = LocalDateTime.now();
            CouponPolicy couponPolicy = new FixedCouponPolicy(1L, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(endedAt, actual)
            );
        }
    }

    @Nested
    @DisplayName("[AfterCouponPolicy] 단위 테스트")
    class AfterCouponPolicyTest {

        @Test
        @DisplayName("[성공] 정책 만료일 계산 테스트 - 특정일 이후의 날짜 반환")
        void calcExpireTime_successTest_forFixed() {
            // Given
            int afterDay = 10;
            LocalDateTime baseTime = LocalDateTime.now();
            CouponPolicy couponPolicy = new AfterCouponPolicy(1L, afterDay);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(baseTime.plusDays(afterDay), actual)
            );
        }
    }

    @Nested
    @DisplayName("[MixedCouponPolicy] 단위 테스트")
    class MixedCouponPolicyTest {

        @Test
        @DisplayName("[성공] MIXED 정책 만료일 계산 테스트 - 특정일 이후가 고정 만료일보다 더 임박한 경우 특정일 이후 날짜 반환")
        void calcExpireTime_successTest_forMixedWhenAfterDayIsSooner() {
            // Given
            LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endedAt = baseTime.plusDays(20);
            int afterDay = 2;

            CouponPolicy couponPolicy = new MixedCouponPolicy(1L, afterDay, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(baseTime.plusDays(afterDay), actual)
            );
        }

        @Test
        @DisplayName("[성공] MIXED 정책 만료일 계산 테스트 - 고정 만료일이 특정일 이후보다 더 임박한 경우 만료일 날짜 반환")
        void calcExpireTime_successTest_forMixedWhenEndedAtIsSooner() {
            // Given
            LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endedAt = baseTime.plusDays(2);
            int afterDay = 20;

            CouponPolicy couponPolicy = new MixedCouponPolicy(1L, afterDay, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(endedAt, actual)
            );
        }
    }

    @Nested
    @DisplayName("[LegacyCouponPolicy] 단위 테스트")
    class LegacyCouponPolicyTest {
        @Test
        @DisplayName("[성공] AFTER 정책 만료일 계산 테스트 - 특정일 이후 날짜 반환")
        void calcExpireTime_successTest_forAfter() {
            // Given
            int afterDay = 10;
            LocalDateTime baseTime = LocalDateTime.now();
            CouponPolicy couponPolicy = new LegacyCouponPolicy(1L, CouponPolicyType.AFTER, afterDay, null, null);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(baseTime.plusDays(afterDay), actual)
            );
        }

        @Test
        @DisplayName("[성공] FIXED 정책 만료일 계산 테스트 - 고정 만료일 반환")
        void calcExpireTime_successTest_forFixed() {
            // Given
            LocalDateTime endedAt = LocalDateTime.MAX;
            LocalDateTime baseTime = LocalDateTime.now();
            CouponPolicy couponPolicy = new LegacyCouponPolicy(1L, CouponPolicyType.FIXED, null, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(endedAt, actual)
            );
        }

        @Test
        @DisplayName("[성공] MIXED 정책 만료일 계산 테스트 - 특정일 이후가 고정 만료일보다 더 임박한 경우 특정일 이후 날짜 반환")
        void calcExpireTime_successTest_forMixedWhenAfterDayIsSooner() {
            // Given
            LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endedAt = baseTime.plusDays(20);
            int afterDay = 2;

            CouponPolicy couponPolicy = new LegacyCouponPolicy(1L, CouponPolicyType.MIXED, afterDay, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(baseTime.plusDays(afterDay), actual)
            );
        }

        @Test
        @DisplayName("[성공] MIXED 정책 만료일 계산 테스트 - 고정 만료일이 특정일 이후보다 더 임박한 경우 만료일 날짜 반환")
        void calcExpireTime_successTest_forMixedWhenEndedAtIsSooner() {
            // Given
            LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endedAt = baseTime.plusDays(2);
            int afterDay = 20;

            CouponPolicy couponPolicy = new LegacyCouponPolicy(1L, CouponPolicyType.MIXED, afterDay, LocalDateTime.MIN, endedAt);
            // When
            LocalDateTime actual = couponPolicy.calculateExpireDateTime(baseTime);
            // Then
            Assertions.assertAll(
                () -> Assertions.assertEquals(endedAt, actual)
            );
        }
    }
}