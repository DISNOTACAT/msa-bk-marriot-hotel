package com.bkmarriott.reservationservice.reservation.application.outputport;

public interface CouponOutputPort {

  Float getCouponDiscountRate(Long memberCouponId);
}
