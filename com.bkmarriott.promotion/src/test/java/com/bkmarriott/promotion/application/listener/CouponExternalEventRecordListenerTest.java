package com.bkmarriott.promotion.application.listener;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.promotion.application.event.CouponIssuance;
import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Application] [Unit] CouponExternalEventRecordListener Test")
@ExtendWith(MockitoExtension.class)
class CouponExternalEventRecordListenerTest {

    @InjectMocks
    private CouponExternalEventRecordListener listener;

    @Mock private CouponExternalEventRecorder eventRecorder;

    @Test
    @DisplayName("[성공] 이벤트 기록 테스트 - 아웃박스에 이벤트를 저장")
    public void recordMessageHandle_successTest() {
        // Given
        CouponIssuance couponIssuance = Mockito.mock(CouponIssuance.class);
        DomainEventEnvelop envelop = Mockito.mock(DomainEventEnvelop.class);

        Mockito.when(couponIssuance.envelop()).thenReturn(envelop);
        Mockito.when(eventRecorder.record(ArgumentMatchers.any(DomainEventEnvelop.class)))
            .thenReturn(envelop);
        // When & Then
        assertAll(
            () -> Assertions.assertDoesNotThrow(() -> listener.recordMessageHandle(couponIssuance))
        );
    }
}