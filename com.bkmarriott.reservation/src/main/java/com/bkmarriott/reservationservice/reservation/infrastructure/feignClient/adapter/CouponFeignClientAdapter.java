package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.infrastructure.exception.DisabledCouponException;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.CouponClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.coupon.GetMemberCouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponFeignClientAdapter implements CouponOutputPort {

  private final CouponClient couponClient;

  @Override
  public Float getCouponDiscountRate(Long memberCouponId) {
    log.info("[CouponFeignClientAdapter] [getCouponDiscountRate] member couponId ::: {}", memberCouponId);
    GetMemberCouponResponse response = couponClient.getUserCoupon(memberCouponId).getBody();
    if(response == null) {
      throw  new DisabledCouponException("사용할 수 없는 쿠폰입니다.");
    }
    return response.getCoupon().getDiscountRate();
  }
}
