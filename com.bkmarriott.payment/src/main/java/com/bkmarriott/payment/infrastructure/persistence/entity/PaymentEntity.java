package com.bkmarriott.payment.infrastructure.persistence.entity;

import com.bkmarriott.payment.domain.Payment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "m_payment")
@Entity
public class PaymentEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "reservation_id", nullable = false)
  private Long reservationId;

  @Column(name = "original_price", nullable = false)
  private Long originalPrice;

  @Column(name = "final_price", nullable = false)
  private Long finalPrice;

  @Column(name = "payment_Status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentEntityStatus paymentStatus;

  @Column(name = "payment_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentEntityType paymentType;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "member_coupon_id")
  private Long memberCouponId;


  public static PaymentEntity from(Payment payment) {
    return new PaymentEntity(
        payment.getId(),
        payment.getReservationId(),
        payment.getOriginalPrice(),
        payment.getFinalPrice(),
        PaymentEntityStatus.fromDomain(payment.getPaymentStatus()),
        PaymentEntityType.fromDomain(payment.getPaymentType()),
        payment.getTransactionId(),
        payment.getMemberCouponId()
    );
  }

  public Payment toDomain() {
    return new Payment(
        id,
        reservationId,
        originalPrice,
        finalPrice,
        paymentStatus.toDomain(),
        paymentType.toDomain(),
        transactionId,
        memberCouponId
    );
  }

  public PaymentEntity setPaid(String transactionId) {
    this.transactionId = transactionId;
    this.paymentStatus = PaymentEntityStatus.PAID;
    return PaymentEntity.this;
  }

  public PaymentEntity setRefunded() {
    this.paymentStatus = PaymentEntityStatus.REFUNDED;
    return PaymentEntity.this;
  }

  public PaymentEntity createByUser(Long userId) {
    super.createdByUser(userId);
    return PaymentEntity.this;
  }

  public PaymentEntity updateByUser(Long userId) {
    super.updatedByUser(userId);
    return PaymentEntity.this;
  }
}
