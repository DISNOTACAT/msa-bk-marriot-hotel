package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.util.UUID;

public interface CouponExternalEventRecorder {

    DomainEventEnvelop<CouponIssuanceEvent> record(DomainEventEnvelop<CouponIssuanceEvent> envelop);

    void recordToPublished(UUID recordId);
}
