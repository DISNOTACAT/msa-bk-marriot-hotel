package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePayment {

  private Long paymentId;
  private Long reservationId;

}
