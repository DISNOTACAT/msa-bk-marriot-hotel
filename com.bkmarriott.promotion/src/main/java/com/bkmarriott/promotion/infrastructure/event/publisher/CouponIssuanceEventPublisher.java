package com.bkmarriott.promotion.infrastructure.event.publisher;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponIssuanceEventPublisher {

    private final KafkaTemplate<String, DomainEventEnvelop<? extends DomainEvent>> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.coupon.issuance}")
    private String couponIssuanceTopic;

    public void publish(DomainEventEnvelop<CouponIssuanceEvent> eventEnvelop) {
        CompletableFuture<SendResult<String, DomainEventEnvelop<? extends DomainEvent>>> result
            = kafkaTemplate.send(couponIssuanceTopic, eventEnvelop);

        log.info("[CouponIssuanceEventPublisher] [publish] eventType ::: {}, result ::: {}", eventEnvelop.getEventType(), result.isDone());
    }
}
