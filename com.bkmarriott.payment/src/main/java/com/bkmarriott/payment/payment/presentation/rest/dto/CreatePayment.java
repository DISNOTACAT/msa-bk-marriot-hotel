package com.bkmarriott.payment.payment.presentation.rest.dto;

import com.bkmarriott.payment.payment.domain.Payment;
import com.bkmarriott.payment.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.payment.domain.vo.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatePayment {

  @Getter
  @AllArgsConstructor
  public static class Requset {

    private int originalPrice;
    private PaymentType paymentType;
    private Long memberCouponId;

    public Payment toDomain() {
      return new Payment(
          null,
          null,
          originalPrice,
          originalPrice,
          PaymentStatus.PENDING,
          PaymentType.CARD,
          null,
          memberCouponId
      );
    }
  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long id;
    private Long reservationId;
    private int originalPrice;
    private int finalPrice;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private String transactionId;
    private Long memberCouponId;

    public static Response from(Payment payment) {
      return new Response(
          payment.getId(),
          payment.getReservationId(),
          payment.getOriginalPrice(),
          payment.getFinalPrice(),
          payment.getPaymentStatus(),
          payment.getPaymentType(),
          payment.getTransactionId(),
          payment.getMemberCouponId()
      );
    }
  }


}
