package com.bkmarriott.coupon.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MemberCoupon {
    private Long id;
    private Coupon coupon;
    private Long memberId;
    private LocalDateTime issuanceAt;
    private LocalDateTime spendingAt;
    private LocalDateTime expiredAt;

    public MemberCoupon(Long id, Coupon coupon, Long memberId, LocalDateTime issuanceAt, LocalDateTime spendingAt,
                        LocalDateTime expiredAt) {
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
        this.issuanceAt = issuanceAt;
        this.spendingAt = spendingAt;
        this.expiredAt = expiredAt;
    }
}
