package com.bkmarriott.promotion.infrastructure.schedule.scheduler;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.infrastructure.event.adapter.CouponIssuanceEventPublisher;
import com.bkmarriott.promotion.infrastructure.persistence.adapter.CouponEventPersistenceAdapter;
import com.bkmarriott.promotion.infrastructure.persistence.entity.CouponIssuanceOutboxEntity;
import com.bkmarriott.promotion.infrastructure.persistence.util.EventConverter;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponIssuancePublishScheduler {

    private final CouponEventPersistenceAdapter couponEventPersistenceAdapter;
    private final CouponIssuanceEventPublisher couponIssuanceEventPublisher;
    private final EventConverter eventConverter;

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void scheduleCouponIssuancePublish() {
        log.debug("[CouponIssuancePublishScheduler] [scheduleCouponIssuancePublish] startedAt ::: {}", LocalDateTime.now());

        List<CouponIssuanceOutboxEntity> beforePublished = couponEventPersistenceAdapter.findBeforePublished();
        beforePublished.forEach(entity -> {
            DomainEventEnvelop<CouponIssuanceEvent> eventEnvelop = parseFrom(entity);
            couponIssuanceEventPublisher.publish(eventEnvelop);
            entity.toPublished();
        });

        log.debug("[CouponIssuancePublishScheduler] [scheduleCouponIssuancePublish] endedAt ::: {}", LocalDateTime.now());
    }

    private DomainEventEnvelop<CouponIssuanceEvent> parseFrom(CouponIssuanceOutboxEntity entity) {
        CouponIssuanceEvent event =
            eventConverter.convertFromJson(entity.getPayload(), entity.getEventType());

        return DomainEventEnvelop.valueOf(
            event, entity.getUuid(), entity.getCreatedAt(), entity.getEventType(), entity.getSource()
        );
    }
}
