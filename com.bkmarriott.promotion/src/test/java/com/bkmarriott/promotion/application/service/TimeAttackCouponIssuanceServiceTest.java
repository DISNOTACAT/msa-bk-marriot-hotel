package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import com.bkmarriott.promotion.application.outputport.TimeAttackCouponIssuer;
import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.domain.vo.CouponIssuanceResult;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Application] [Unit] TimeAttackCouponService Test")
@ExtendWith(MockitoExtension.class)
class TimeAttackCouponIssuanceServiceTest {

    private static final TimeAttackCouponIssuance MOCK_COUPON_ISSUANCE;
    private static final Promotion MOCK_PROMOTION;
    private static final CouponIssuanceResult MOCK_COUPON_ISSUANCE_RESULT;

    static {
        MOCK_COUPON_ISSUANCE = Mockito.mock(TimeAttackCouponIssuance.class);
        Mockito.when(MOCK_COUPON_ISSUANCE.getTargetPromotionId()).thenReturn(1L);
        Mockito.when(MOCK_COUPON_ISSUANCE.getRequestUserId()).thenReturn(1L);
        Mockito.when(MOCK_COUPON_ISSUANCE.getRequestTime()).thenReturn(LocalDateTime.now());

        MOCK_PROMOTION = Mockito.mock(Promotion.class);
        Mockito.when(MOCK_PROMOTION.getCouponId()).thenReturn(1L);

        MOCK_COUPON_ISSUANCE_RESULT = Mockito.mock(CouponIssuanceResult.class);
    }

    @InjectMocks
    private TimeAttackCouponIssuanceService timeAttackCouponIssuanceService;

    @Mock private PromotionService promotionService;
    @Mock private TimeAttackCouponIssuer timeAttackCouponOutputPort;
    @Mock private CouponExternalEventRecorder couponExternalEventRecorder;
    @Mock private ApplicationEventPublishService applicationEventPublishService;

    @Test
    @DisplayName("[성공] 선착순 쿠폰 발급 테스트 - 선착순 내에 신청하고 중복되지 않은 경우 true 반환")
    void issue_successTest_successToIssuance() {
        // Given
        Mockito.when(MOCK_COUPON_ISSUANCE_RESULT.isSuccess(Mockito.any())).thenReturn(true);

        Mockito.when(promotionService.findPromotionInProgress(
            ArgumentMatchers.anyLong(), ArgumentMatchers.any(LocalDateTime.class)
        )).thenReturn(MOCK_PROMOTION);

        Mockito.when(timeAttackCouponOutputPort.tryIssuance(
            ArgumentMatchers.any(TimeAttackCouponIssuance.class)
        )).thenReturn(MOCK_COUPON_ISSUANCE_RESULT);
        // When
        boolean actual = timeAttackCouponIssuanceService.issue(MOCK_COUPON_ISSUANCE);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(actual)
        );
    }

    @Test
    @DisplayName("[성공] 선착순 쿠폰 발급 테스트 - 선착순 내에 들지 못했거나 중복된 경우 false 반환")
    void issue_successTest_failureToIssuance() {
        // Given
        Mockito.when(MOCK_COUPON_ISSUANCE_RESULT.isSuccess(Mockito.any())).thenReturn(false);

        Mockito.when(promotionService.findPromotionInProgress(
            ArgumentMatchers.anyLong(), ArgumentMatchers.any(LocalDateTime.class)
        )).thenReturn(MOCK_PROMOTION);

        Mockito.when(timeAttackCouponOutputPort.tryIssuance(
            ArgumentMatchers.any(TimeAttackCouponIssuance.class)
        )).thenReturn(MOCK_COUPON_ISSUANCE_RESULT);
        // When
        boolean actual = timeAttackCouponIssuanceService.issue(MOCK_COUPON_ISSUANCE);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(actual)
        );
    }
}