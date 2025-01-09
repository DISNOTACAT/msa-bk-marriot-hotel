package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.CouponPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "m_coupon_policy")
@Entity(name = "CouponPolicy")
public class CouponPolicyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponPolicyEntityType type;

    private Integer afterDay;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    public CouponPolicyEntity(Long id, CouponPolicyEntityType type, Integer afterDay, LocalDateTime startedAt,
                              LocalDateTime endedAt) {
        super.createdBySystem();
        this.id = id;
        this.type = type;
        this.afterDay = afterDay;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public CouponPolicy toDomain() {
        return new CouponPolicy(id, type.toDomain(), afterDay, startedAt, endedAt);
    }

    public static CouponPolicyEntity from(CouponPolicy couponPolicy) {
        return new CouponPolicyEntity(
                couponPolicy.getId(),
                CouponPolicyEntityType.valueOf(couponPolicy.getType().name()),
                couponPolicy.getAfterDay(),
                couponPolicy.getStartedAt(),
                couponPolicy.getEndedAt()
        );
    }
}
