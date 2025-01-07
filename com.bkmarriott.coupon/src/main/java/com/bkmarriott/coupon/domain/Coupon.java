package com.bkmarriott.coupon.domain;

import lombok.Getter;

@Getter
public class Coupon {
    private Long id;
    private CouponPolicy couponPolicy;
    private String name;
    private Float discountRate;

    public Coupon(Long id, CouponPolicy couponPolicy, String name, Float discountRate) {
        this.id = id;
        this.couponPolicy = couponPolicy;
        this.name = name;
        this.discountRate = discountRate;
    }
}
