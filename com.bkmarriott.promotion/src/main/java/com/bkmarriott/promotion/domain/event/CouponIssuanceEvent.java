package com.bkmarriott.promotion.domain.event;

import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter                    // for jackson serialize
@NoArgsConstructor(access = AccessLevel.PRIVATE)    // for jackson serialize
public class CouponIssuanceEvent implements DomainEvent {

    private Long promotionId;
    private Long couponId;
    private Long userId;
    private LocalDateTime requestDateTime;

    public CouponIssuanceEvent(Long promotionId, Long couponId, Long userId, LocalDateTime requestDateTime) {
        this.promotionId = promotionId;
        this.couponId = couponId;
        this.userId = userId;
        this.requestDateTime = requestDateTime;
    }

    public static CouponIssuanceEvent from(Promotion promotion, TimeAttackCouponIssuance issuance) {
        return new CouponIssuanceEvent(
            issuance.getTargetPromotionId(),
            promotion.getCouponId(),
            issuance.getRequestUserId(),
            issuance.getRequestTime()
        );
    }
}
