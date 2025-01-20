package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.outputport.CouponExternalMessageSender;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponExternalEventSendService {

    private final CouponExternalMessageSender messageSender;

    public DomainEventEnvelop<CouponIssuanceEvent> send(DomainEventEnvelop<CouponIssuanceEvent> envelop) {
        log.debug("[CouponExternalEventSendService] [send] envelop ::: {}", envelop);
        this.validateEvent(envelop);
        return messageSender.sendMessage(envelop);
    }

    private void validateEvent(DomainEventEnvelop<CouponIssuanceEvent> envelop) {
        if (envelop == null || envelop.getEvent() == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
    }
}
