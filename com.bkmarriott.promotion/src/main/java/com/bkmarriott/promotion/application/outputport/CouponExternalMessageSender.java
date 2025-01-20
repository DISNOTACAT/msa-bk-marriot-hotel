package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;

public interface CouponExternalMessageSender {

    DomainEventEnvelop<CouponIssuanceEvent> sendMessage(DomainEventEnvelop<CouponIssuanceEvent> message);
}
