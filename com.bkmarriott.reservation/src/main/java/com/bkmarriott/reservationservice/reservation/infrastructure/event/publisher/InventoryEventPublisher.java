package com.bkmarriott.reservationservice.reservation.infrastructure.event.publisher;

import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventPublisher {

    private final KafkaTemplate<String, RoomInventoryEvent> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.inventory}")
    private String inventoryChangeTopic;

    public void publish(RoomInventoryEvent event){
        log.info("[RoomInventoryEventPublisher] [publish] topic ::: {}", inventoryChangeTopic);

        CompletableFuture<SendResult<String, RoomInventoryEvent>> result = kafkaTemplate.send(inventoryChangeTopic, event);

        log.info("[RoomInventoryEventPublisher] [publish] changeType ::: {}, result ::: {}", event.getChangeType(), result.isDone());
    }
}
