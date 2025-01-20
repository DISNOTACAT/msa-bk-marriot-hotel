package com.bkmarriott.promotion.application.listener;

import com.bkmarriott.promotion.application.event.CouponIssuance;
import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import com.bkmarriott.promotion.application.service.CouponExternalEventSendService;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponExternalEventMessageListener {

    private static final String EVENT_ASYNC_TASK_EXECUTOR = "EVENT_ASYNC_TASK_EXECUTOR";

    private final CouponExternalEventSendService eventSendService;
    private final CouponExternalEventRecorder eventRecorder;

    @Async(EVENT_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageHandle(CouponIssuance couponIssuance) {
        log.debug("[CouponExternalEventMessageListener] [publishEventHandle] async event publish running, event ::: {}", couponIssuance);

        DomainEventEnvelop<CouponIssuanceEvent> envelop = couponIssuance.envelop();
        envelop = eventSendService.send(envelop);
        eventRecorder.recordToPublished(envelop.getEventId());
    }
}
