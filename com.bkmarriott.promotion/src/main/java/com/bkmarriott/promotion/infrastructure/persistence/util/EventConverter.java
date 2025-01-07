package com.bkmarriott.promotion.infrastructure.persistence.util;

import com.bkmarriott.promotion.domain.event.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventConverter {

    private final ObjectMapper objectMapper;

    public <T extends DomainEvent> String convertToJson(T domainEvent) {
        try {
            return objectMapper.writeValueAsString(domainEvent);
        } catch (JsonProcessingException exception) {
            log.error("[EventConverter] [convertToJson] exception ::: ", exception);
            throw new RuntimeException("[EventConverter] [convertToJson] failure");
        }
    }

    public <T extends DomainEvent> T convertFromJson(String eventJson, String eventType) {
        try {
            Class<?> type = Class.forName(eventType);
            return objectMapper.readValue(
                eventJson, objectMapper.getTypeFactory().constructType(type)
            );
        } catch (JsonProcessingException | ClassNotFoundException exception) {
            log.error("[EventConverter] [convertFromJson] exception ::: ", exception);
            throw new RuntimeException("[EventConverter] [convertFromJson] failure");
        }
    }
}
