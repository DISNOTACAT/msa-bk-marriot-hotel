package com.bkmarriott.promotion.domain.vo;

import com.bkmarriott.promotion.domain.Promotion;

public class CouponIssuanceResult {

    private final Long issuanceCount;
    private final boolean isDuplicated;

    private CouponIssuanceResult(Long issuanceCount, boolean isDuplicated) {
        this.issuanceCount = issuanceCount;
        this.isDuplicated = isDuplicated;
    }

    public static CouponIssuanceResult valueOf(Long issuanceCount, boolean isDuplicated) {
        return new CouponIssuanceResult(issuanceCount, isDuplicated);
    }

    public boolean isSuccess(Promotion promotion) {
        return promotion.isMaxIssuanceNotReached(issuanceCount.intValue()) && !isDuplicated;
    }
}
