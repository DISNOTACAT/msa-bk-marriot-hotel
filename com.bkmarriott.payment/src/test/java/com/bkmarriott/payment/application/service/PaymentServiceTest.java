package com.bkmarriott.payment.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bkmarriott.payment.application.dto.CreatePaymentDto;
import com.bkmarriott.payment.application.exception.SavePaymentFailureException;
import com.bkmarriott.payment.application.outputport.PaymentCommandOutputPort;
import com.bkmarriott.payment.application.outputport.PaymentQueryOutputPort;
import com.bkmarriott.payment.domain.Payment;
import com.bkmarriott.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.domain.vo.PaymentType;
import com.bkmarriott.payment.infrastructure.persistence.entity.PaymentEntity;
import com.bkmarriott.payment.presentation.rest.exception.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] PaymentService Unit Test")
class PaymentServiceTest {

  @InjectMocks
  private PaymentService paymentService;
  @Mock
  private PaymentCommandOutputPort paymentCommandOutputPort;
  @Mock
  private PaymentQueryOutputPort paymentQueryOutputPort;

  @Test
  @DisplayName("[결제 정보 저장 성공 테스트] 예약 정보가 주어였을 시, 새로운 결제 정보를 저장한다.")
  void create_payment_successTest() {
    //given
    CreatePaymentDto mockDto = new CreatePaymentDto(
        123L,
        "123456789",
        "2030-01-31",
        "123",
        3L,
        330000L,
        300000L,
        PaymentStatus.PENDING,
        PaymentType.CARD
    );
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
    Mockito.when(paymentCommandOutputPort.save(ArgumentMatchers.any(Payment.class)))
        .thenReturn(payment);

    //when
    Payment actual = paymentService.createPayment(mockDto);

    //then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mockDto.reservationId(), actual.getReservationId()),
        () -> Assertions.assertEquals(mockDto.originalPrice(), actual.getOriginalPrice()),
        () -> Assertions.assertEquals(mockDto.finalPrice(), actual.getFinalPrice()),
        () -> Assertions.assertEquals(PaymentStatus.PAID, actual.getPaymentStatus()),
        () -> Assertions.assertEquals(PaymentType.CARD, actual.getPaymentType()),
        () -> Assertions.assertNotNull(actual.getTransactionId()),
        () -> Assertions.assertEquals(mockDto.appliedCoupon(), actual.getMemberCouponId())
    );
  }

  @Test
  @DisplayName("[결제 환불 성공 테스트] 결제 아이디가 주어였을 시, 결제 상태를 업데이트한다.")
  void refund_payment_successTest() {
    //given
    Long paymentId = 1L;

    Payment payment = new Payment(
        paymentId,
        123L,
        330000L,
        300000L,
        PaymentStatus.REFUNDED,
        PaymentType.CARD,
        "123123",
        3L
    );
    PaymentEntity mockEntity = PaymentEntity.from(payment);

    Mockito.when(paymentQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(mockEntity.toDomain()));
    Mockito.when(paymentCommandOutputPort.refund(ArgumentMatchers.anyLong()))
        .thenReturn(payment);

    //when
    Payment actual = paymentService.refundPayment(paymentId);

    //then
    Assertions.assertAll(
        () -> Assertions.assertEquals(payment.getId(), paymentId),
        () -> Assertions.assertEquals(payment.getReservationId(), actual.getReservationId()),
        () -> Assertions.assertEquals(payment.getOriginalPrice(), actual.getOriginalPrice()),
        () -> Assertions.assertEquals(payment.getFinalPrice(), actual.getFinalPrice()),
        () -> Assertions.assertEquals(PaymentStatus.REFUNDED, actual.getPaymentStatus()),
        () -> Assertions.assertEquals(payment.getPaymentType(), actual.getPaymentType()),
        () -> Assertions.assertNotNull(payment.getTransactionId(), actual.getTransactionId()),
        () -> Assertions.assertEquals(payment.getMemberCouponId(), actual.getMemberCouponId())
    );
  }

  @Test
  @DisplayName("[결제 실패 테스트] 결제 정보 저장 중 오류가 발생할 경우, 예외를 반환한다.")
  void create_payment_failureTest() {

    // Given
    CreatePaymentDto createPaymentDto = new CreatePaymentDto(
        123L,
        "123456789",
        "2030-01-31",
        "123",
        3L,
        330000L,
        300000L,
        PaymentStatus.PENDING,
        PaymentType.CARD
    );

    Mockito.when(paymentCommandOutputPort.save(ArgumentMatchers.any(Payment.class)))
        .thenThrow(new SavePaymentFailureException("결제 정보 생성에 실패하였습니다."));

    // When & Then
    assertThatThrownBy(() -> paymentService.createPayment(createPaymentDto))
        .isInstanceOf(SavePaymentFailureException.class)
        .hasMessage("결제 정보 생성에 실패하였습니다.");
  }

  @Test
  @DisplayName("[결제 환불 조회 실패 테스트] 존재하지 않는 결제 아이디가 주어였을 시, 예외를 반환한다.")
  void null_find_payment_failureTest() {
    // Given
    Long paymentId = 1L;
    Mockito.when(paymentQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> paymentService.refundPayment(paymentId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("존재하지 않는 결제 정보");
  }

}
