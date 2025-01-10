package com.bkmarriott.payment.infrastructure.adapter;

import com.bkmarriott.payment.domain.Payment;
import com.bkmarriott.payment.domain.vo.PaymentStatus;
import com.bkmarriott.payment.domain.vo.PaymentType;
import com.bkmarriott.payment.infrastructure.RepositoryTest;
import com.bkmarriott.payment.infrastructure.persistence.adapter.PaymentCommandAdapter;
import com.bkmarriott.payment.infrastructure.persistence.adapter.PaymentQueryAdapter;
import com.bkmarriott.payment.infrastructure.persistence.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] Payment Repository Unit Test")
@RepositoryTest
class PaymentAdaptorTest {

  @Autowired
  private PaymentCommandAdapter paymentCommandAdapter;
  @Autowired
  private PaymentQueryAdapter paymentQueryAdapter;
  @Autowired
  private PaymentRepository paymentRepository;


  @Test
  @DisplayName("[결제 단일 조회 성공 테스트] 결제 아이디를 이용하여 결제 정보를 조회한다. ")
  void find_successTest() {
    //Given
    Long paymentId = 1L;

    Payment mock = new Payment(
        1L,
        101L,
        500000L,
        450000L,
        PaymentStatus.PAID,
        PaymentType.CARD,
        "TXN12345",
        1L
    );

    //When
    Payment actual = paymentQueryAdapter.findById(paymentId).get();
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mock.getReservationId(), actual.getReservationId()),
        () -> Assertions.assertEquals(mock.getOriginalPrice(), actual.getOriginalPrice()),
        () -> Assertions.assertEquals(mock.getFinalPrice(), actual.getFinalPrice()),
        () -> Assertions.assertEquals(mock.getPaymentStatus(), actual.getPaymentStatus()),
        () -> Assertions.assertEquals(mock.getPaymentType(), actual.getPaymentType()),
        () -> Assertions.assertEquals(mock.getTransactionId(), actual.getTransactionId()),
        () -> Assertions.assertEquals(mock.getMemberCouponId(), actual.getMemberCouponId())
    );
  }

  @Test
  @DisplayName("[결제 정보 저장 성공 테스트] 예약 정보가 주어였을 시, 새로운 결제 정보를 저장한다. ")
  void save_successTest() {
    //Given
    Payment mock = new Payment(
        1L,
        101L,
        500000L,
        450000L,
        PaymentStatus.PAID,
        PaymentType.CARD,
        "TXN12345",
        1L
    );

    //When
    Payment actual = paymentCommandAdapter.save(mock);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mock.getReservationId(), actual.getReservationId()),
        () -> Assertions.assertEquals(mock.getOriginalPrice(), actual.getOriginalPrice()),
        () -> Assertions.assertEquals(mock.getFinalPrice(), actual.getFinalPrice()),
        () -> Assertions.assertEquals(mock.getPaymentStatus(), actual.getPaymentStatus()),
        () -> Assertions.assertEquals(mock.getPaymentType(), actual.getPaymentType()),
        () -> Assertions.assertEquals(mock.getTransactionId(), actual.getTransactionId()),
        () -> Assertions.assertEquals(mock.getMemberCouponId(), actual.getMemberCouponId())
    );
  }

  @Test
  @DisplayName("[결제 환불 성공 테스트] 결제 아이디가 주어였을 시, 결제 상태를 환불로 수정한다. ")
  void refund_successTest() {
    //Given
    Long paymentId = 1L;
    Payment mock = new Payment(
        1L,
        101L,
        500000L,
        450000L,
        PaymentStatus.PAID,
        PaymentType.CARD,
        "TXN12345",
        1L
    );

    //When
    Payment actual = paymentCommandAdapter.refund(paymentId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mock.getReservationId(), actual.getReservationId()),
        () -> Assertions.assertEquals(mock.getOriginalPrice(), actual.getOriginalPrice()),
        () -> Assertions.assertEquals(mock.getFinalPrice(), actual.getFinalPrice()),
        () -> Assertions.assertEquals(PaymentStatus.REFUNDED, actual.getPaymentStatus()),
        () -> Assertions.assertEquals(mock.getPaymentType(), actual.getPaymentType()),
        () -> Assertions.assertEquals(mock.getTransactionId(), actual.getTransactionId()),
        () -> Assertions.assertEquals(mock.getMemberCouponId(), actual.getMemberCouponId())
    );
  }
}
