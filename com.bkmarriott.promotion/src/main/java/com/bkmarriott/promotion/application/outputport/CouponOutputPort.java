package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;

public interface CouponOutputPort {

    void issueCoupon(CouponIssuanceEvent couponIssuanceEvent);
}
