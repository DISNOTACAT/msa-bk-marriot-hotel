package com.bkmarriott.promotion.application.schedule.sceduler;

import com.bkmarriott.promotion.application.outputport.CouponExternalEventReader;
import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import com.bkmarriott.promotion.application.service.CouponExternalEventSendService;
import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponIssuancePublishScheduler {

    private static final long TIME_LIMIT_MINUTES_FOR_OLD = 20;
    private static final long SCHEDULER_INTERVAL_MINUTES = 20;

    private final CouponExternalEventReader eventReader;
    private final CouponExternalEventRecorder eventRecorder;
    private final CouponExternalEventSendService eventSendService;

    /**
     * TIME_LIMIT_MINUTES_FOR_OLD 분 동안 발급되지 못한 엔티티를
     * 주기적으로 카프카에 발행하는 스케줄러
     * */
    @Transactional
    @Scheduled(fixedRate = SCHEDULER_INTERVAL_MINUTES * 60000)
    public void scheduleCouponIssuanceEventPublish() {
        LocalDateTime scheduledAt = LocalDateTime.now();
        log.info("[CouponIssuancePublishScheduler] [scheduleCouponIssuancePublish] startedAt ::: {}", scheduledAt);

        List<DomainEventEnvelop<CouponIssuanceEvent>> envelops = eventReader.readAllBeforePublished();

        for (DomainEventEnvelop<CouponIssuanceEvent> envelop : envelops) {
            if (isOldEvent(envelop, scheduledAt)) {
                eventSendService.send(envelop);
                eventRecorder.recordToPublished(envelop.getEventId());
            }
        }
        log.debug("[CouponIssuancePublishScheduler] [scheduleCouponIssuancePublish] endedAt ::: {}", LocalDateTime.now());
    }

    private boolean isOldEvent(DomainEventEnvelop<?> envelop, LocalDateTime baseTime) {
        Duration duration = Duration.between(envelop.getCreatedAt(), baseTime);
        return duration.toMinutes() >= TIME_LIMIT_MINUTES_FOR_OLD;
    }
}
