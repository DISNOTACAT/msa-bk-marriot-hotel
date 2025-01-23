package com.bkmarriott.reservationservice.reservation.infrastructure.batch.item;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.EventEntityType;

public record EventTypeCount (
        EventEntityType type,
        Long count
){}
