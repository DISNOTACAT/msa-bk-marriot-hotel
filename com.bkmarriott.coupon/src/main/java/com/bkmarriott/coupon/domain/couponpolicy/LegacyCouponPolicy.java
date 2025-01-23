package com.bkmarriott.coupon.domain.couponpolicy;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LegacyCouponPolicy implements CouponPolicy {

    private final Long id;
    private final CouponPolicyType type;

    private final Integer afterDay;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    @Override
    public LocalDateTime calculateExpireDateTime(LocalDateTime baseTime) {
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
