package com.bkmarriott.coupon.domain.couponpolicy;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;

public class CouponPolicyFactory {

    public static CouponPolicy generateCouponPolicy(
        CouponPolicyType type, Long id, Integer afterDay,
        LocalDateTime startedAt, LocalDateTime endedAt
    ) {
        return switch (type) {
            case MIXED -> new MixedCouponPolicy(id, afterDay, startedAt, endedAt);
            case FIXED -> new FixedCouponPolicy(id, startedAt, endedAt);
            case AFTER -> new AfterCouponPolicy(id, afterDay);
        };
    }
}
