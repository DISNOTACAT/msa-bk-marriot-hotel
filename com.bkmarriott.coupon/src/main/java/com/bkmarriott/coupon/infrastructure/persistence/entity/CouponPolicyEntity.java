package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.couponpolicy.CouponPolicy;
import com.bkmarriott.coupon.domain.couponpolicy.CouponPolicyFactory;
import com.bkmarriott.coupon.domain.couponpolicy.LegacyCouponPolicy;
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
import lombok.ToString;

@ToString
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

    public CouponPolicyEntity(Long id) {
        this.id = id;
    }

    public CouponPolicy toDomain() {
        return CouponPolicyFactory.generateCouponPolicy(
            type.toDomain(), id, afterDay, startedAt, endedAt
        );
    }

    public static CouponPolicyEntity from(LegacyCouponPolicy couponPolicy) {
        return new CouponPolicyEntity(
                couponPolicy.getId(),
                CouponPolicyEntityType.valueOf(couponPolicy.getType().name()),
                couponPolicy.getAfterDay(),
                couponPolicy.getStartedAt(),
                couponPolicy.getEndedAt()
        );
    }

    public static CouponPolicyEntity generateWithId(Long id) {
        return new CouponPolicyEntity(id);
    }
}
