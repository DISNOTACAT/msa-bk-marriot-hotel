package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.vo.CouponPolicyType;

public enum CouponPolicyEntityType {
    FIXED, AFTER, MIXED;

    public CouponPolicyType toDomain() {
        return CouponPolicyType.valueOf(this.name());
    }
}
