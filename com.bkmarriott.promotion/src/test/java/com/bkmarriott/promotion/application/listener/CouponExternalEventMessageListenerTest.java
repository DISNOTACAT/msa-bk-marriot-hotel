package com.bkmarriott.promotion.application.listener;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.promotion.application.event.CouponIssuance;
import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import com.bkmarriott.promotion.application.service.CouponExternalEventSendService;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Application] [Unit] CouponExternalEventMessageListener Test")
@ExtendWith(MockitoExtension.class)
class CouponExternalEventMessageListenerTest {

    @InjectMocks
    private CouponExternalEventMessageListener listener;

    @Mock private CouponExternalEventSendService eventSendService;
    @Mock private CouponExternalEventRecorder eventRecorder;

    @Test
    @DisplayName("[성공] 메세진 발송 테스트 - 이벤트를 발송한 뒤 아웃박스에 저장된 데이터를 발송됨으로 변경")
    public void sendMessageHandle_successTest() {
        // Given
        CouponIssuance couponIssuance = Mockito.mock(CouponIssuance.class);
        DomainEventEnvelop envelop = Mockito.mock(DomainEventEnvelop.class);

        Mockito.when(couponIssuance.envelop()).thenReturn(envelop);
        Mockito.when(envelop.getEventId()).thenReturn(UUID.randomUUID());

        Mockito.when(eventSendService.send(ArgumentMatchers.any())).thenReturn(envelop);
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() -> listener.sendMessageHandle(couponIssuance))
        );
    }
}