package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.infrastructure.exception.PaymentFailureException;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.PaymentClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.CreatePayment.Requset;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.UpdatePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFeignClientAdapter implements PaymentOutputPort {

  private final PaymentClient paymentClient;

  @Override
  public Long createPayment(Requset request) {
    Long paymentId = paymentClient.createPayment(request, getCurrentUserId());
    if(paymentId == null) {
      throw new PaymentFailureException("Failed to create payment");
    }
    return paymentId;
  }

  @Override
  public void updateReservationId(Long paymentId, Long reservationId) {
    log.info("[PaymentFeignClientAdapter] [updateReservationId] paymentId ::: {}, reservationId ::: {}, getCurrentUserId ::: {}", paymentId, reservationId, getCurrentUserId());

    paymentClient.updateReservationId(new UpdatePayment(paymentId, reservationId), getCurrentUserId());
  }

  private String getCurrentUserId() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes.getRequest().getHeader("X-User-Id");
  }
}
