package com.bkmarriott.payment.infrastructure.persistence.entity;

import com.bkmarriott.payment.domain.vo.PaymentType;
import java.util.Objects;

public enum PaymentEntityType {

  CARD, CASH;

  public static PaymentEntityType fromDomain(PaymentType status) {
    return PaymentEntityType.valueOf(status.name());
  }

  public PaymentType toDomain() {
    for(PaymentType type : PaymentType.values()) {
      if(Objects.equals(type.name(), this.name())) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid status: " + this.name());
  }
}
