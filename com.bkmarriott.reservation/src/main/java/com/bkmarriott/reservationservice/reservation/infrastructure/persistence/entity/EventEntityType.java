package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.event.EventType;

public enum EventEntityType {

    PREPARED, ROLLBACK;

    public static EventEntityType fromDomain(EventType eventType){
        return EventEntityType.valueOf(eventType.name());
    }

}
