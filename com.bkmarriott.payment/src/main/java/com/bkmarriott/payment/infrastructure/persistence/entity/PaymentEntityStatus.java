package com.bkmarriott.payment.infrastructure.persistence.entity;

import com.bkmarriott.payment.domain.vo.PaymentStatus;
import java.util.Objects;

public enum PaymentEntityStatus {

  PENDING, PAID, REFUNDED, CANCELLED, REJECTED;

  public static PaymentEntityStatus fromDomain(PaymentStatus status) {
    return PaymentEntityStatus.valueOf(status.name());
  }

  public PaymentStatus toDomain() {
    for(PaymentStatus status : PaymentStatus.values()) {
      if(Objects.equals(status.name(), this.name())) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid status: " + this.name());
  }
}
