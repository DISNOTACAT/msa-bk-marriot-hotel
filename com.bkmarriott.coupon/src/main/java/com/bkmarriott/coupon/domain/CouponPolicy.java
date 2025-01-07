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

    public CouponPolicy(Long id, CouponPolicyType type, Integer afterDay, LocalDateTime startedAt,
                        LocalDateTime endedAt) {
        this.id = id;
        this.type = type;
        this.afterDay = afterDay;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public LocalDateTime calcExpireTime(LocalDateTime baseTime) {
        return switch (type) {
            case FIXED -> endedAt;
            case AFTER -> baseTime.plusDays(afterDay);
            case MIXED -> {
                LocalDateTime endedAtForAfterDay = baseTime.plusDays(afterDay);
                yield (endedAt.isBefore(endedAtForAfterDay)) ? endedAt : endedAtForAfterDay;
            }
        };
    }
}
