package com.bkmarriott.promotion.application.event;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public record CouponIssuance(DomainEventEnvelop<CouponIssuanceEvent> envelop,
                             LocalDateTime publishedAt) {

}
