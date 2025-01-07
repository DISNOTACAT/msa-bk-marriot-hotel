package com.bkmarriott.coupon.presentation.event.util;

import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
@RequiredArgsConstructor
public class CouponIssuanceEventDeserializer implements Deserializer<DomainEventEnvelop<CouponIssuanceEvent>> {

    private final ObjectMapper objectMapper;

    @Override
    public DomainEventEnvelop<CouponIssuanceEvent> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, objectMapper.getTypeFactory()
                .constructParametricType(DomainEventEnvelop.class, CouponIssuanceEvent.class));
        } catch (IOException e) {
            log.error("[CouponIssuanceEventDeserializer] [deserialize] topic ::: {}, data ::: {}, error ::: ", topic, data, e);
            throw new RuntimeException(e);
        }
    }
}
