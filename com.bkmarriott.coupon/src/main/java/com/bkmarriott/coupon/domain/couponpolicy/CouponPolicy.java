package com.bkmarriott.coupon.domain.couponpolicy;

import java.time.LocalDateTime;

public interface CouponPolicy {

    Long getId();

    LocalDateTime calculateExpireDateTime(LocalDateTime baseTime);
}
