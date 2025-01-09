package com.bkmarriott.coupon.presentation.event.subscriber;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.coupon.application.exception.EventDuplicateException;
import com.bkmarriott.coupon.application.service.CouponEventService;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
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

@DisplayName("[Presentation] [Unit] CouponIssuanceEventSubscriber Test")
@ExtendWith(MockitoExtension.class)
class CouponIssuanceEventSubscriberTest {

    @InjectMocks
    private CouponIssuanceEventSubscriber eventSubscriber;

    @Mock
    private CouponEventService couponEventService;

    @Test
    @DisplayName("[성공] 이벤트를 소모 테스트 - 이벤트를 소모하여 유저쿠폰을 생성")
    public void consume_successTest() {
        // Given
        CouponIssuanceEvent event = new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        DomainEventEnvelop<CouponIssuanceEvent> eventDomainEventEnvelop = DomainEventEnvelop.of(event, "testSource");

        Mockito.when(couponEventService.issueCoupon(ArgumentMatchers.any()))
            .thenReturn(Mockito.mock(UserCoupon.class));
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() ->
                eventSubscriber.consume(eventDomainEventEnvelop)
            )
        );
    }

    @Test
    @DisplayName("[성공] 이벤트를 소모 테스트 - 이벤트 중복 예외가 발생한 경우 예외 내용을 로그")
    public void consume_successTest_catchEventDuplicateException() {
        // Given
        CouponIssuanceEvent event = new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        DomainEventEnvelop<CouponIssuanceEvent> eventDomainEventEnvelop = DomainEventEnvelop.of(event, "testSource");

        Mockito.when(couponEventService.issueCoupon(ArgumentMatchers.any()))
            .thenThrow(new EventDuplicateException());

        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() ->
                eventSubscriber.consume(eventDomainEventEnvelop)
            )
        );
    }
}