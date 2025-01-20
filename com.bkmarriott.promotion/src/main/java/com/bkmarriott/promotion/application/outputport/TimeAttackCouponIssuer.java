package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.vo.CouponIssuanceResult;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;

public interface TimeAttackCouponIssuer {

    CouponIssuanceResult tryIssuance(TimeAttackCouponIssuance issuance);
}
