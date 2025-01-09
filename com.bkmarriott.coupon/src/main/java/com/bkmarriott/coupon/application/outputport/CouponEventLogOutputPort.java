package com.bkmarriott.coupon.application.outputport;

import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;

public interface CouponEventLogOutputPort {

    boolean isExistedCouponLog(String logId);

    String saveLog(DomainEventEnvelop<CouponIssuanceEvent> envelop);
}
