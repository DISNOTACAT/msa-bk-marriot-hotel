package com.bkmarriott.reservationservice.reservation.infrastructure.event.adapter;


import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryMessageSender;
import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.infrastructure.event.publisher.InventoryEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventSendAdapter implements InventoryMessageSender {

    private final InventoryEventPublisher inventoryEventPublisher;

    @Override
    public void sendMessage(RoomInventoryEvent event) {
        log.info("[InventoryEventSendAdapter] [sendMessage]");

        inventoryEventPublisher.publish(event);
    }
}
