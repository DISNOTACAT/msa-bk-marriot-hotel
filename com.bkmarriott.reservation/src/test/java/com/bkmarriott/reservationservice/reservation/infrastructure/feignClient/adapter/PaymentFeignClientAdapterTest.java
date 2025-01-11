package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.*;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.PaymentClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentDto;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Infrastructure] PaymentFeignClientAdapterTest Unit Test")
public class PaymentFeignClientAdapterTest {

    @InjectMocks PaymentFeignClientAdapter paymentFeignClientAdapter;
    @Mock PaymentClient paymentClient;

    Reservation reservation;

    @BeforeEach
    void setUp(){
        reservation = new Reservation(
                1L,
                1L,
                1L,
                LocalDate.parse("2025-02-01"),
                LocalDate.parse("2025-02-02"),
                RoomType.DELUXE,
                ReservationStatus.PENDING,
                null
        );
    }

    @Test
    @DisplayName("[성공] 결제 FeignClient 테스트 - 예약 ID와 결제 정보를 사용하여 결제 FeignClient 호출하고 Payment 반환한다.")
    public void processPayment_successTest(){
        // Given
        Long originalPrice = 190000L;
        Long finalPrice = 171000L;
        Long appliedCoupon = 1L;
        PaymentForCreate paymentForCreate = new PaymentForCreate(null, "credit_card", "4111111111111111", "12/25", "123", appliedCoupon, originalPrice, finalPrice);
        PaymentDto mockPayment = new PaymentDto(1L, reservation.getReservationId(), originalPrice, finalPrice,"PAID", "transactionalId", appliedCoupon);

        Mockito.when(paymentClient.processPayment(PaymentRequestDto.from(paymentForCreate, reservation.getReservationId()), reservation.getUserId(), "CUSTOMER")).thenReturn(mockPayment);

        // When
        Payment result =  paymentFeignClientAdapter.processPayment(paymentForCreate, reservation);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.reservationId(), mockPayment.reservationId()),
                () -> Assertions.assertEquals(result.originalPrice(), mockPayment.originalPrice()),
                () -> Assertions.assertEquals(result.finalPrice(), mockPayment.finalPrice()),
                () -> Assertions.assertEquals(result.paymentType(), mockPayment.paymentType()),
                () -> Assertions.assertEquals(result.transactionalId(), mockPayment.transactionalId()),
                () -> Assertions.assertEquals(result.appliedCoupon(), mockPayment.appliedCoupon())
        );
    }

    @Test
    @DisplayName("[성공] 환불 FeignClient 테스트 - 결제 ID를 가지고 환불 FeignClient 호출하고 Payment 반환한다.")
    public void processRefund_successTest(){
        // Given
        Long paymentId = 1L;
        PaymentDto mockPayment = new PaymentDto(paymentId, 1L, 190000L, 171000L,"REFUNDED", "transactionalId", 1L);

        Mockito.when(paymentClient.processRefund(paymentId, reservation.getUserId(), "CUSTOMER")).thenReturn(mockPayment);

        // When
        Payment result =  paymentFeignClientAdapter.processRefund(paymentId, reservation);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.paymentId(), paymentId),
                () -> Assertions.assertEquals(result.reservationId(), mockPayment.reservationId()),
                () -> Assertions.assertEquals(result.originalPrice(), mockPayment.originalPrice()),
                () -> Assertions.assertEquals(result.finalPrice(), mockPayment.finalPrice()),
                () -> Assertions.assertEquals(result.paymentType(), mockPayment.paymentType()),
                () -> Assertions.assertEquals(result.transactionalId(), mockPayment.transactionalId()),
                () -> Assertions.assertEquals(result.appliedCoupon(), mockPayment.appliedCoupon())
        );
    }

    @Test
    @DisplayName("[실패] 환불 FeignClient 테스트 - PaymentClient 예외 발생 시 Fallback 메서드를 통해 null 반환한다.")
    public void processRefund_failureTest(){
        // Given
        Long paymentId = 1L;

        String errorMessage = "FeignClient Exception";
        Throwable throwable = new RuntimeException(errorMessage);

        // When
        Payment fallbackCharge = paymentFeignClientAdapter.fallbackProcessRefund(paymentId, throwable);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNull(fallbackCharge)
        );
    }
}