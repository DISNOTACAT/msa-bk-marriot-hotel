package com.bkmarriott.coupon.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter                   // for jackson serialize
@NoArgsConstructor(access = AccessLevel.PRIVATE)    // for jackson serialize
public class DomainEventEnvelop<T extends DomainEvent> {

    private T event;
    private UUID eventId;
    private LocalDateTime createdAt;
    private String eventType;
    private String source;

    private DomainEventEnvelop(
        T event, UUID eventId, LocalDateTime createdAt, String eventType, String source
    ) {
        this.event = event;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.eventType = eventType;
        this.source = source;
    }

    public static <T extends DomainEvent> DomainEventEnvelop<T> of(T event, String source) {
        return new DomainEventEnvelop<>(
            event, UUID.randomUUID(), LocalDateTime.now(), event.getClass().getTypeName(), source
        );
    }

    public static <T extends DomainEvent> DomainEventEnvelop<T> valueOf(
        T event, String uuid, LocalDateTime createdAt, String eventType, String source
    ) {
        return new DomainEventEnvelop<>(event, UUID.fromString(uuid), createdAt, eventType, source);
    }
}
