package com.bkmarriott.payment.presentaion.rest.controller;

import com.bkmarriott.payment.application.dto.CreatePaymentDto;
import com.bkmarriott.payment.application.service.PaymentService;
import com.bkmarriott.payment.domain.Payment;
import com.bkmarriott.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.domain.vo.PaymentType;
import com.bkmarriott.payment.presentation.rest.controller.PaymentClientController;
import com.bkmarriott.payment.presentation.rest.dto.PaymentRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(PaymentClientController.class)
@DisplayName("[Presentation] class PaymentClientController Unit Test")
class PaymentClientControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PaymentService paymentService;

  @Test
  @DisplayName("[결제 정보 저장 성공 테스트] 예약 정보가 주어였을 시, 새로운 결제 정보를 저장한다.")
  void create_payment_successTest() throws Exception {
    PaymentRequestDto request = new PaymentRequestDto(
        123L,
        "CARD",
        "123456789",
        "2030-01-31",
        "123",
        3L,
        330000L,
        300000L
    );

    String requestUrl = "/api/v1/payments";

    Payment payment = new Payment(
        1L,
        123L,
        330000L,
        300000L,
        PaymentStatus.PAID,
        PaymentType.CARD,
        "123123",
        3L
    );

    Mockito.when(paymentService.createPayment(Mockito.any(CreatePaymentDto.class)))
        .thenReturn(payment);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", 1L)
            .header("X-Role", "CUSTOMER"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentId").value(payment.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.reservationId").value(payment.getReservationId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.originalPrice").value(payment.getOriginalPrice()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.finalPrice").value(payment.getFinalPrice()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentType").value(payment.getPaymentType().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.transactionalId").value(payment.getTransactionId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.appliedCoupon").value(payment.getMemberCouponId()));
  }


  @Test
  @DisplayName("[결제 환불 성공 테스트] 결제 아이디가 주어질 시, 환불 상태로 변경하여 결제 정보를 반환한다.")
  void refund_payment_successTest() throws Exception {
    // Given
    String paymentId = "1";
    String requestUrl = "/api/v1/payments/refund/" + paymentId;
    Payment payment = new Payment(
        1L,
        101L,
        500000L,
        450000L,
        PaymentStatus.REFUNDED,
        PaymentType.CARD,
        "TXN12345",
        1L
    );
    Mockito.when(paymentService.refundPayment(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(payment);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.patch(requestUrl)
            .param("paymentId", paymentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", 1L)
            .header("X-Role", "CUSTOMER"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentId").value(payment.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.reservationId").value(payment.getReservationId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.originalPrice").value(payment.getOriginalPrice()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.finalPrice").value(payment.getFinalPrice()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentType").value(payment.getPaymentType().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.transactionalId").value(payment.getTransactionId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.appliedCoupon").value(payment.getMemberCouponId()));
  }
}
