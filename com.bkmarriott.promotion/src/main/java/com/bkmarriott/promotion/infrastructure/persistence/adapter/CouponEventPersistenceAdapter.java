package com.bkmarriott.promotion.infrastructure.persistence.adapter;

import com.bkmarriott.promotion.application.outputport.CouponOutputPort;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.infrastructure.persistence.entity.CouponIssuanceOutboxEntity;
import com.bkmarriott.promotion.infrastructure.persistence.repository.CouponIssuanceOutboxRepository;
import com.bkmarriott.promotion.infrastructure.persistence.util.EventConverter;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponEventPersistenceAdapter implements CouponOutputPort {

    private final CouponIssuanceOutboxRepository outboxRepository;
    private final EventConverter eventConverter;

    @Override
    public void issueCoupon(CouponIssuanceEvent couponIssuanceEvent) {
        log.debug(
            "[CouponEventPersistenceAdapter] [issueCoupon] promotionId ::: {}, couponId ::: {}, userId ::: {}",
            couponIssuanceEvent.getPromotionId(), couponIssuanceEvent.getCouponId(), couponIssuanceEvent.getUserId()
        );
        CouponIssuanceOutboxEntity outboxEntity = parseFromEvent(couponIssuanceEvent);
        outboxRepository.save(outboxEntity);
    }

    private CouponIssuanceOutboxEntity parseFromEvent(CouponIssuanceEvent event) {
        DomainEventEnvelop<CouponIssuanceEvent> envelop = DomainEventEnvelop.of(
            event, "[promotion service] [CouponEventPersistenceAdapter] [issueCoupon]"
        );
        String eventJson = eventConverter.convertToJson(event);
        return CouponIssuanceOutboxEntity.from(envelop, eventJson);
    }

    @Transactional
    public List<CouponIssuanceOutboxEntity> findBeforePublished() {
        return outboxRepository.findAllByIsPublishedIsFalse();
    }
}
