package com.bkmarriott.coupon.domain.couponpolicy;

import java.time.LocalDateTime;
import java.util.Objects;

public class FixedCouponPolicy implements CouponPolicy{

    private final Long id;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    public FixedCouponPolicy(Long id, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt cannot be null");
        this.endedAt = Objects.requireNonNull(endedAt, "endedAt cannot be null");
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public LocalDateTime calculateExpireDateTime(LocalDateTime baseTime) {
        return endedAt;
    }
}
