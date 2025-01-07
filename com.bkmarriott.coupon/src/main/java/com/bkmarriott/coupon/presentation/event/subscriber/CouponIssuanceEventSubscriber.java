package com.bkmarriott.coupon.presentation.event.subscriber;

import com.bkmarriott.coupon.application.exception.EventDuplicateException;
import com.bkmarriott.coupon.application.service.CouponEventService;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponIssuanceEventSubscriber {

    private final CouponEventService couponEventService;

    @KafkaListener(
        topics = "${spring.kafka.consumer.topic.coupon.issuance}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "couponIssuanceEventListener"
    )
    public void consume(DomainEventEnvelop<CouponIssuanceEvent> envelop) {
        log.info("[CouponIssuanceEventSubscriber] [consume] domainEventEnvelop ::: {}", envelop);
        try {
            UserCoupon userCoupon = couponEventService.issueCoupon(envelop);
            log.debug("[CouponIssuanceEventSubscriber] [consume] UserCouponId ::: {}", userCoupon.getId());
        } catch (EventDuplicateException exception) {
            log.error("[CouponIssuanceEventSubscriber] [consume] event duplicated");
        }
    }
}
