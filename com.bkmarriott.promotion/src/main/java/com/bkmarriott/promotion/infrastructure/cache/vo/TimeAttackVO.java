package com.bkmarriott.promotion.infrastructure.cache.vo;

import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class TimeAttackVO {

    private static final String KEY_FORMAT = ":time-attack:%d:issued:users";

    @Getter private final Long promotionId;
    @Getter private final Long userId;
    private final String key;

    private TimeAttackVO(Long promotionId, Long userId) {
        this.promotionId = promotionId;
        this.userId = userId;
        this.key = String.format(KEY_FORMAT, promotionId);
    }

    public static TimeAttackVO from(TimeAttackCouponIssuance issuance) {
        return new TimeAttackVO(issuance.getTargetPromotionId(), issuance.getRequestUserId());
    }

    public String key() {
        return key;
    }
}
