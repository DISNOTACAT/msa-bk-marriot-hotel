package com.bkmarriott.promotion.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.promotion.domain.vo.PromotionPeriod;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("[Domain] [Unit] Promotion Test")
class PromotionTest {

    private static final PromotionPeriod TEST_PERIOD = PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX);

    private static final Integer TEST_MAX_ISSUANCE_COUNT = 10;
    private static final Promotion TEST_PROMOTION = new Promotion(1L, 1L, "test", "test", TEST_MAX_ISSUANCE_COUNT, TEST_PERIOD);

    @Test
    @DisplayName("[성공] Promotion 객체 생성 테스트 - 정상적인 입력값으로 객체 생성")
    void createPromotion_successTest() {
        // Given
        Long promotionId = 1L;
        Long couponId = 2L;
        String name = "name";
        String description = "description";
        Integer maxIssuance = 100;
        // When & Then
        Promotion promotion = new Promotion(promotionId, couponId, name, description, maxIssuance, TEST_PERIOD);
        Assertions.assertAll(
            () -> Assertions.assertEquals(couponId, promotion.getCouponId())
        );
    }

    @Test
    @DisplayName("[성공] Promotion 기간 확인 테스트 - 프로모션 기간 내의 날짜 정보가 주어진 경우 true 반환")
    void promotionIsInProgress_successTest_inProgress() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        // When
        boolean actual = TEST_PROMOTION.isInProgressWhen(now);
        // Then
        assertAll(
            () -> Assertions.assertTrue(actual)
        );
    }

    @Test
    @DisplayName("[성공] Promotion 기간 확인 테스트 - 프로모션 기간 외의 날짜 정보가 주어진 경우 false 반환")
    void promotionIsInProgress_successTest_notInProgress() {
        // Given
        LocalDateTime now = LocalDateTime.MAX;
        // When
        boolean actual = TEST_PROMOTION.isInProgressWhen(now);
        // Then
        assertAll(
            () -> Assertions.assertFalse(actual)
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 한도 확인 테스트 - 발급 한도에 도달하지 않은 경우 true 반환")
    void promotionIsMaxIssuanceNotReached_successTest_notReached() {
        // Given
        int notReachedCount = TEST_MAX_ISSUANCE_COUNT - 1;
        // When
        boolean actual = TEST_PROMOTION.isMaxIssuanceNotReached(notReachedCount);
        // Then
        assertAll(
            () -> Assertions.assertTrue(actual)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    @DisplayName("[성공] 쿠폰 발급 한도 확인 테스트 - 발급 한도에 도달한 경우 false 반환")
    void promotionIsMaxIssuanceNotReached_successTest_reached(Integer reachedCount) {
        // Given & When
        boolean actual = TEST_PROMOTION.isMaxIssuanceNotReached(reachedCount);
        // Then
        assertAll(
            () -> Assertions.assertFalse(actual)
        );
    }
}