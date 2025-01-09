package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import static com.bkmarriott.reservationservice.reservation.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.reservationservice.reservation.infrastructure.config.FeignOkHttpConfig;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.CreatePayment;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.UpdatePayment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="payment-service", configuration = FeignOkHttpConfig.class)
public interface PaymentClient {

  @PostMapping(value = "/api/v1/payments/internal")
  Long createPayment(
      @RequestBody CreatePayment.Requset request,
      @RequestHeader(HEADER_USER_ID) String userId);

  @RequestMapping(value = "/api/v1/payments/internal", method = RequestMethod.PATCH)
  void updateReservationId(
      @RequestBody UpdatePayment updatePayment,
      @RequestHeader(HEADER_USER_ID) String userId);
}
