package com.bkmarriott.promotion.infrastructure.persistence.entity;


import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.infrastructure.persistence.util.EventConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_coupon_issuance_outbox")
@Entity(name = "couponIssuanceOutbox")
public class CouponIssuanceOutboxEntity {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String outboxId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isPublished;

    public static CouponIssuanceOutboxEntity from(
        DomainEventEnvelop<CouponIssuanceEvent> envelop, String eventJson
    ) {
        return new CouponIssuanceOutboxEntity(
            String.valueOf(envelop.getEventId()), envelop.getEventType(), eventJson,
            envelop.getSource(), envelop.getCreatedAt(), false
        );
    }

    public void toPublished() {
        isPublished = true;
    }

    public DomainEventEnvelop<CouponIssuanceEvent> toEnvelop(EventConverter eventConverter) {
        return eventConverter.parseToEnvelopFrom(this);
    }
}
