package com.bkmarriott.coupon.domain;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntityType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponPolicy {
    private Long id;
    private CouponPolicyType type;
    private Integer afterDay;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public CouponPolicy(Long id, CouponPolicyEntityType type, Integer afterDay, LocalDateTime startedAt,
                        LocalDateTime endedAt) {
        this.id = id;
        this.type = CouponPolicyType.valueOf(type.name());
        this.afterDay = afterDay;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
