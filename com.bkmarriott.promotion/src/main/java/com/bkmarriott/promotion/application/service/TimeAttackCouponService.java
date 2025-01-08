package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.outputport.CouponOutputPort;
import com.bkmarriott.promotion.application.outputport.TimeAttackCouponOutputPort;
import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.vo.CouponIssuanceResult;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimeAttackCouponService {

    private final PromotionService promotionService;
    private final TimeAttackCouponOutputPort timeAttackCouponOutputPort;
    private final CouponOutputPort couponOutputPort;

    public boolean issueTimeAttackCoupon(TimeAttackCouponIssuance couponIssuance) {
        Promotion promotion = promotionService.findPromotionInProgress(
            couponIssuance.getTargetPromotionId(), couponIssuance.getRequestTime()
        );
        return this.tryToIssuanceCoupon(couponIssuance, promotion);
    }

    private boolean tryToIssuanceCoupon(final TimeAttackCouponIssuance issuance, final Promotion promotion) {
        CouponIssuanceResult result = timeAttackCouponOutputPort.tryIssuance(issuance);
        if (!result.isSuccess(promotion)) {
            return false;
        }
        CouponIssuanceEvent issuanceEvent = CouponIssuanceEvent.from(promotion, issuance);
        couponOutputPort.issueCoupon(issuanceEvent);
        return true;
    }
}
