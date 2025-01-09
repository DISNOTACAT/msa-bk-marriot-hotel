package com.bkmarriott.coupon.application.service;

import static org.assertj.core.api.Assertions.*;

import com.bkmarriott.coupon.application.exception.EventDuplicateException;
import com.bkmarriott.coupon.application.outputport.CouponEventLogOutputPort;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
import java.time.LocalDateTime;
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

@DisplayName("[Application] [Unit] CouponEventService Test")
@ExtendWith(MockitoExtension.class)
class CouponEventServiceTest {

    private static DomainEventEnvelop<CouponIssuanceEvent> testDomainEnvelop;

    @InjectMocks
    private CouponEventService couponEventService;

    @Mock private UserCouponService userCouponService;
    @Mock private CouponEventLogOutputPort couponEventLogOutputPort;

    @BeforeAll
    static void beforeAll() {
        CouponIssuanceEvent event = new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        testDomainEnvelop = DomainEventEnvelop.of(event, "testSource");
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 테스트 - 이벤트를 소모하여 유저에게 쿠폰 발급후 UserCoupon 반환")
    void issueCoupon_successTest() {
        // Given
        Mockito.when(couponEventLogOutputPort.isExistedCouponLog(ArgumentMatchers.any()))
            .thenReturn(false);

        Mockito.when(userCouponService.issueCoupon(ArgumentMatchers.any()))
            .thenReturn(Mockito.mock(UserCoupon.class));
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() ->
                couponEventService.issueCoupon(testDomainEnvelop)
            )
        );
    }

    @Test
    @DisplayName("[실패] 쿠폰 발급 테스트 - 중복된 이벤트로 판단되는 경우 예외 발생")
    void issueCoupon_failureTest() {
        // Given
        Mockito.when(couponEventLogOutputPort.isExistedCouponLog(ArgumentMatchers.any()))
            .thenReturn(true);
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() -> couponEventService.issueCoupon(testDomainEnvelop))
                .isInstanceOf(EventDuplicateException.class)
        );
    }
}