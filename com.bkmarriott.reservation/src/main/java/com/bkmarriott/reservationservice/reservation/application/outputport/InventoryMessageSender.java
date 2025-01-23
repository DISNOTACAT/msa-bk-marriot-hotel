package com.bkmarriott.reservationservice.reservation.application.outputport;

import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;

public interface InventoryMessageSender {

    void sendMessage(RoomInventoryEvent event);

}
