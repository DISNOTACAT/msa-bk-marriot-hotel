package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.outputport.TimeAttackCouponIssuer;
import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.domain.vo.CouponIssuanceResult;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TimeAttackCouponIssuanceService {

    private final PromotionService promotionService;
    private final TimeAttackCouponIssuer timeAttackCouponIssuer;
    private final ApplicationEventPublishService eventPublishService;

    @Transactional
    public boolean issue(TimeAttackCouponIssuance couponIssuance) {
        log.debug("[TimeAttackCouponIssuanceService] [issue] couponIssuance ::: {}", couponIssuance);
        Promotion promotion = promotionService.findPromotionInProgress(
            couponIssuance.getTargetPromotionId(), couponIssuance.getRequestTime()
        );
        return this.tryToIssuanceCoupon(couponIssuance, promotion);
    }

    private boolean tryToIssuanceCoupon(final TimeAttackCouponIssuance issuance, final Promotion promotion) {
        CouponIssuanceResult result = timeAttackCouponIssuer.tryIssuance(issuance);
        if (result.isSuccess(promotion)) {
            DomainEventEnvelop<CouponIssuanceEvent> envelop = DomainEventEnvelop.of(
                CouponIssuanceEvent.from(promotion, issuance),
                this.getClass().getName()
            );
            eventPublishService.publish(envelop);
            return true;
        }
        return result.isSuccess(promotion);
    }
}
