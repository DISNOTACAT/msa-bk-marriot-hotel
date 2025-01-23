package com.bkmarriott.coupon.domain.couponpolicy;

import java.time.LocalDateTime;
import java.util.Objects;

public class AfterCouponPolicy implements CouponPolicy {

    private final Long id;
    private final Integer afterDay;

    public AfterCouponPolicy(Long id, Integer afterDay) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.afterDay = Objects.requireNonNull(afterDay, "afterDay cannot be null");
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public LocalDateTime calculateExpireDateTime(LocalDateTime baseTime) {
        return baseTime.plusDays(afterDay);
    }
}
