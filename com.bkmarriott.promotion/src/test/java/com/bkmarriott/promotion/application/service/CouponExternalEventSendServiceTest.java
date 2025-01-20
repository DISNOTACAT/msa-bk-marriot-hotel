package com.bkmarriott.promotion.application.service;

import static org.assertj.core.api.Assertions.*;

import com.bkmarriott.promotion.application.outputport.CouponExternalMessageSender;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Unit] [Application] CouponExternalEventSendService Test")
@ExtendWith(MockitoExtension.class)
class CouponExternalEventSendServiceTest {

    @InjectMocks
    private CouponExternalEventSendService couponExternalEventSendService;

    @Mock
    private CouponExternalMessageSender couponExternalMessageSender;

    @Test
    @DisplayName("[성공] 이벤트 발행 테스트 - 이벤트를 발행한 뒤 DomainEventEnvelop 객체 반환")
    public void send_successTest() {
        // Given
        DomainEventEnvelop envelop = Mockito.mock(DomainEventEnvelop.class);
        CouponIssuanceEvent event = Mockito.mock(CouponIssuanceEvent.class);
        Mockito.when(envelop.getEvent()).thenReturn(event);

        Mockito.when(couponExternalMessageSender.sendMessage(envelop))
            .thenReturn(envelop);
        // When
        DomainEventEnvelop actual = couponExternalEventSendService.send(envelop);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(envelop.getEvent(), actual.getEvent())
        );
    }

    @Test
    @DisplayName("[실패] 이벤트 발행 테스트 - DomainEventEnvelop 객체가 null 인 경우 예외 발생")
    public void send_failureTest_nullEnvelop() {
        // Given
        DomainEventEnvelop envelop = null;
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() -> couponExternalEventSendService.send(envelop))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event cannot be null")
        );
    }

    @Test
    @DisplayName("[실패] 이벤트 발행 테스트 - Event 객체가 null 인 경우 예외 발생")
    public void send_failureTest_nullEvent() {
        // Given
        DomainEventEnvelop envelop = Mockito.mock(DomainEventEnvelop.class);
        Mockito.when(envelop.getEvent()).thenReturn(null);
        // When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(() -> couponExternalEventSendService.send(envelop))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event cannot be null")
        );
    }
}