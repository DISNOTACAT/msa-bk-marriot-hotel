package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.coupon;

import java.time.LocalDateTime;
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

    public LocalDateTime calcCouponExpireTime(LocalDateTime baseTime) {
        return couponPolicy.calcExpireTime(baseTime);
    }
}
