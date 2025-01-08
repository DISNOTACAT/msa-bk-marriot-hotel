package com.bkmarriott.promotion.infrastructure.persistence.entity;


import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isPublished;

    public static CouponIssuanceOutboxEntity from(
        DomainEventEnvelop<CouponIssuanceEvent> envelop,
        String eventJson
    ) {
        return new CouponIssuanceOutboxEntity(
            null, envelop.getEventType(), eventJson, envelop.getSource(),
            envelop.getEventId().toString(), envelop.getCreatedAt(), false
        );
    }

    public void toPublished() {
        isPublished = true;
    }
}
