package com.bkmarriott.promotion.domain.vo;

import java.time.LocalDateTime;

public class TimeAttackCouponIssuance {

    private final Long requestUserId;
    private final Long targetPromotionId;
    private final LocalDateTime requestTime;

    public TimeAttackCouponIssuance(Long requestUserId, Long targetPromotionId,
        LocalDateTime requestTime) {
        this.requestUserId = requestUserId;
        this.targetPromotionId = targetPromotionId;
        this.requestTime = requestTime;
    }

    public Long getRequestUserId() {
        return requestUserId;
    }

    public Long getTargetPromotionId() {
        return targetPromotionId;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }
}
