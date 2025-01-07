package com.bkmarriott.promotion.domain;

import com.bkmarriott.promotion.domain.vo.PromotionPeriod;
import java.time.LocalDateTime;

public class Promotion {

    private final Long promotionId;
    private final Long couponId;

    private final String name;
    private final String description;
    private final Integer maxIssuance;

    private final PromotionPeriod promotionPeriod;

    public Promotion(
        Long promotionId, Long couponId, String name, String description,
        Integer maxIssuance, PromotionPeriod promotionPeriod
    ) {
        this.promotionId = promotionId;
        this.couponId = couponId;
        this.name = name;
        this.description = description;
        this.maxIssuance = maxIssuance;
        this.promotionPeriod = promotionPeriod;
    }

    public boolean isInProgressWhen(LocalDateTime targetDateTime) {
        return promotionPeriod.isContains(targetDateTime);
    }

    public boolean isMaxIssuanceNotReached(Integer currentIssuance) {
        return currentIssuance < maxIssuance;
    }

    public Long getCouponId() {
        return couponId;
    }
}
