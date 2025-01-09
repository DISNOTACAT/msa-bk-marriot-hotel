package com.bkmarriott.payment.payment.presentation.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePayment {

  private Long paymentId;
  private Long reservationId;

}
