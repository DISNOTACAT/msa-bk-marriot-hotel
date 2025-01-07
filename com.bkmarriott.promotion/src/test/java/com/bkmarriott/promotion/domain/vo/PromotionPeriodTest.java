package com.bkmarriott.promotion.domain.vo;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] PromotionPeriod VO Test")
class PromotionPeriodTest {

    @Test
    @DisplayName("[성공] PromotionPeriod 객체 생성 테스트 - 정상적인 입력값으로 객체 생성")
    void promotionPeriodConstruct_successTest() {
        // Given
        LocalDateTime start = LocalDateTime.MIN;
        LocalDateTime end = LocalDateTime.MAX;
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() -> PromotionPeriod.valueOf(start, end))
        );
    }

    @Test
    @DisplayName("[실패] PromotionPeriod 객체 생성 테스트 - 시작일자가 종료일자보다 이후인 경우 예외 발생")
    void promotionPeriodConstruct_failureTest() {
        // Given
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() -> PromotionPeriod.valueOf(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 날짜는 종료일자보다 이후일 수 없습니다.")
        );
    }

    @Test
    @DisplayName("[성공] PromotionPeriod 날짜 포함 테스트 - 기간 내의 날짜가 주어진 경우 true 반환")
    void promotionPeriodIsContains_successTest() {
        // Given
        PromotionPeriod promotionPeriod = PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX);
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(promotionPeriod.isContains(LocalDateTime.now()))
        );
    }

    @Test
    @DisplayName("[성공] PromotionPeriod 날짜 포함 테스트 - 기간 이후의 날짜가 주어진 경우 false 반환")
    void promotionPeriodIsContains_failureTest_afterPeriod() {
        // Given
        PromotionPeriod promotionPeriod = PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX);
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(promotionPeriod.isContains(LocalDateTime.MAX))
        );
    }

    @Test
    @DisplayName("[성공] PromotionPeriod 날짜 포함 테스트 - 기간 이전의 날짜가 주어진 경우 false 반환")
    void promotionPeriodIsContains_failureTest_beforePeriod() {
        // Given
        PromotionPeriod promotionPeriod = PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX);
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(promotionPeriod.isContains(LocalDateTime.MIN))
        );
    }
}