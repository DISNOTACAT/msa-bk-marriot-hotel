package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.event.CouponIssuance;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicationEventPublishService {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(DomainEventEnvelop<CouponIssuanceEvent> envelop) {
        LocalDateTime publishedAt = LocalDateTime.now();
        log.debug("[ApplicationEventPublishService] [publish] envelop ::: {}", envelop);

        CouponIssuance couponIssuance = new CouponIssuance(envelop, publishedAt);
        eventPublisher.publishEvent(couponIssuance);
    }
}
