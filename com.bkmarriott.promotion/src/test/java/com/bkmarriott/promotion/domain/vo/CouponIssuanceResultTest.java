package com.bkmarriott.promotion.domain.vo;

import com.bkmarriott.promotion.domain.Promotion;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] CouponIssuanceResult VO Test")
class CouponIssuanceResultTest {

    @Test
    @DisplayName("[성공] 객체 생성 테스트 - 유효한 정보가 주어진 경우 객체를 생성한다.")
    void couponIssuanceResultConstruct_successTest() {
        // Given
        Long issuanceCount = 1L;
        boolean isDuplicated = true;
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() -> CouponIssuanceResult.valueOf(issuanceCount, isDuplicated))
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 성공 여부 확인 테스트 - 발급 한도에 도달하지 않고 중복 발급되지 않았을 시 true 반환")
    void couponIssuanceResultIsSuccess_successTest() {
        // Given
        Promotion promotion = new Promotion(
            1L, 1L, "test", "test", 100, PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX)
        );
        Long issuanceCount = 1L;
        boolean isDuplicated = false;
        CouponIssuanceResult result = CouponIssuanceResult.valueOf(issuanceCount, isDuplicated);

        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(result.isSuccess(promotion))
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 성공 여부 확인 테스트 - 발급 한도에 도달한 경우 false 반환")
    void couponIssuanceResultIsSuccess_successTest_reached() {
        // Given
        Integer maxIssuance = 100;
        Promotion promotion = new Promotion(
            1L, 1L, "test", "test", maxIssuance, PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX)
        );
        Long issuanceCount = (long) (maxIssuance + 1);
        boolean isDuplicated = false;
        CouponIssuanceResult result = CouponIssuanceResult.valueOf(issuanceCount, isDuplicated);

        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(result.isSuccess(promotion))
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 성공 여부 확인 테스트 - 중복 발급된 경우 false 반환")
    void couponIssuanceResultIsSuccess_successTest_duplicated() {
        // Given
        Promotion promotion = new Promotion(
            1L, 1L, "test", "test", 100, PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX)
        );
        Long issuanceCount = 1L;
        boolean isDuplicated = true;
        CouponIssuanceResult result = CouponIssuanceResult.valueOf(issuanceCount, isDuplicated);

        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(result.isSuccess(promotion))
        );
    }
}