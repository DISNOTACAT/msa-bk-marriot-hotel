package com.bkmarriott.coupon.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserCoupon {

    private Long id;
    private Coupon coupon;
    private Long userId;
    private LocalDateTime issuedAt;
    private LocalDateTime spentAt;
    private LocalDateTime expiredAt;
    private Long version;

    public UserCoupon(
        Long id, Coupon coupon, Long userId,
        LocalDateTime issuedAt, LocalDateTime spentAt, LocalDateTime expiredAt
    ) {
        this.id = id;
        this.coupon = coupon;
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.spentAt = spentAt;
        this.expiredAt = expiredAt;
    }

    public static UserCoupon generateWithoutIdAndSpentAt(
        Coupon coupon, Long userId, LocalDateTime issuedAt, LocalDateTime expiredAt
    ) {
        return new UserCoupon(null, coupon, userId, issuedAt, null, expiredAt);
    }

    public UserCoupon updateSpentAt() {
        this.spentAt = LocalDateTime.now();
        return this;
    }
}