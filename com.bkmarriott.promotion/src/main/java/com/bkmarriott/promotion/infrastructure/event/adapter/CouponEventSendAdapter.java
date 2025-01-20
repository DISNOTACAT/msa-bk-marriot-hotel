package com.bkmarriott.promotion.infrastructure.event.adapter;

import com.bkmarriott.promotion.application.outputport.CouponExternalMessageSender;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.infrastructure.event.publisher.CouponIssuanceEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponEventSendAdapter implements CouponExternalMessageSender {

    private final CouponIssuanceEventPublisher eventPublisher;

    @Override
    public DomainEventEnvelop<CouponIssuanceEvent> sendMessage(DomainEventEnvelop<CouponIssuanceEvent> message) {
        log.info("[CouponEventSendAdapter] [sendMessage] message ::: {}", message);
        eventPublisher.publish(message);
        return message;
    }
}
