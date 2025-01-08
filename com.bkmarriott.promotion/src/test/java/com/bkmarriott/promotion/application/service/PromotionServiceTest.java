package com.bkmarriott.promotion.application.service;

import static org.assertj.core.api.Assertions.*;

import com.bkmarriott.promotion.application.outputport.PromotionOutputPort;
import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.vo.PromotionPeriod;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Application] [Unit] PromotionService Test")
@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    private static final Promotion TEST_PROMOTION
        = new Promotion(1L, 1L, " ", " ", 10, PromotionPeriod.valueOf(LocalDateTime.MIN, LocalDateTime.MAX));

    @InjectMocks
    PromotionService promotionService;

    @Mock PromotionOutputPort promotionOutputPort;

    @Test
    @DisplayName("[성공] 프로모션 조회 - 주어진 식별자와 날짜에 진행되는 프로모션이 존재하는 경우 도메인을 반환")
    void findPromotionInProgress_successTest() {
        // Given
        Long validPromotionId = 1L;
        LocalDateTime inProgressDateTime = LocalDateTime.now();

        Mockito.when(promotionOutputPort.findPromotionById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(TEST_PROMOTION));
        // When
        Promotion actual = promotionService.findPromotionInProgress(validPromotionId, inProgressDateTime);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(actual.isInProgressWhen(inProgressDateTime))
        );
    }

    @Test
    @DisplayName("[실패] 프로모션 조회 - 유효하지 않은 식별자가 주어진 경우 예외를 발생")
    void findPromotionInProgress_failureTest_invalidPromotionId() {
        // Given
        Long invalidPromotionId = -1L;
        LocalDateTime inProgressDateTime = LocalDateTime.now();

        Mockito.when(promotionOutputPort.findPromotionById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() ->
                    promotionService.findPromotionInProgress(invalidPromotionId, inProgressDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로모션 정보가 존재하지 않습니다.")
        );
    }

    @Test
    @DisplayName("[실패] 프로모션 조회 - 프로모션 기간 외의 날짜 정보가 주어진 경우 예외를 발생")
    void findPromotionInProgress_failureTest_notInProgress() {
        // Given
        Long validPromotionId = 1L;
        LocalDateTime beforeProgress = LocalDateTime.MAX;

        Mockito.when(promotionOutputPort.findPromotionById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(TEST_PROMOTION));
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() ->
                promotionService.findPromotionInProgress(validPromotionId, beforeProgress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로모션 진행 기간이 아닙니다.")
        );
    }
}