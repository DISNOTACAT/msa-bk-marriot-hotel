package com.bkmarriott.payment.payment.domain;

import com.bkmarriott.payment.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.payment.domain.vo.PaymentType;
import com.bkmarriott.payment.payment.infrastructure.persistence.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

  private Long id;
  private Long reservationId;
  private int originalPrice;
  private int finalPrice;
  private PaymentStatus paymentStatus;
  private PaymentType paymentType;
  private String transactionId;
  private Long memberCouponId;

  public void setPaid(String transactionId) {
    this.transactionId = transactionId;
    this.paymentStatus = PaymentStatus.PAID;
  }
}
