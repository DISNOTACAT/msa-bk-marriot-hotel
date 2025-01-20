package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.util.List;

public interface CouponExternalEventReader {

    List<DomainEventEnvelop<CouponIssuanceEvent>> readAllBeforePublished();
}
