package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment;

import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatePayment {

  @Getter
  @AllArgsConstructor
  public static class Requset {

    private int originalPrice;
    private int finalPrice;
    private PaymentType paymentType;
    private Long memberCouponId;

    public static Requset from(ReservationForCreate reservationForCreate) {
      return new Requset(
          reservationForCreate.getOriginPrice(),
          reservationForCreate.getFinalPrice(),
          PaymentType.CARD,
          reservationForCreate.getMemberCouponId()
      );
    }
  }

}
