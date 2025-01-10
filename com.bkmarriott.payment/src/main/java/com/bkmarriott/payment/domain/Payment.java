package com.bkmarriott.payment.domain;

import com.bkmarriott.payment.application.dto.CreatePaymentDto;
import com.bkmarriott.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.domain.vo.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  private Long id;
  private Long reservationId;
  private Long originalPrice;
  private Long finalPrice;
  private PaymentStatus paymentStatus;
  private PaymentType paymentType;
  private String transactionId;
  private Long memberCouponId;

  public static Payment from(CreatePaymentDto createPaymentDto) {
    return new Payment(
        null,
        createPaymentDto.reservationId(),
        createPaymentDto.originalPrice(),
        createPaymentDto.finalPrice(),
        createPaymentDto.paymentStatus(),
        createPaymentDto.paymentType(),
        null,
        createPaymentDto.appliedCoupon()
    );
  }

  public void setPaid(String transactionId) {
    this.transactionId = transactionId;
    this.paymentStatus = PaymentStatus.PAID;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
