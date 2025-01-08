package com.bkmarriott.reservationservice.reservation.presentation.application.service;

import static com.bkmarriott.reservationservice.reservation.presentation.config.HttpHeaderConstants.HEADER_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.application.service.ReservationService;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationStatus;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@DisplayName("[Application] ReservationService synchronicity test")
@SpringBootTest
@WebAppConfiguration
class ReservationServiceTest {

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private InventoryService inventoryService;

  @BeforeEach
  void setUp() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HEADER_USER_ID, "1000");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(request));
  }


  @Test
  @DisplayName("[단순 순차적 예약 성공 테스트] 동일한 날짜에 사용자 A, B 가 순차적으로 예약을 진행하여 성공한다.")
  void testSequenceReservations()  {
    // Given
    ReservationForCreate reservationForCreate1 = new ReservationForCreate(
        5000L,
        101L,
        LocalDate.of(2025,2,1),
        LocalDate.of(2025,2,2),
        RoomType.STANDARD,
        ReservationStatus.PAID
    );
    ReservationForCreate reservationForCreate2 = new ReservationForCreate(
        5000L,
        101L,
        LocalDate.of(2025,2,1),
        LocalDate.of(2025,2,2),
        RoomType.STANDARD,
        ReservationStatus.PAID
    );

    int originInventoryQuantity = inventoryService.getInventoryQuantityByRoomType(
        101L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.STANDARD
    ).getQuantity();

    // when
    reservationService.createReservation(reservationForCreate1);
    reservationService.createReservation(reservationForCreate2);
    int restInventoryQuantity = inventoryService.getInventoryQuantityByRoomType(
        101L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.STANDARD
    ).getQuantity();

    // then
    System.out.println("originInventoryQuantity = " + originInventoryQuantity);
    System.out.println("restInventoryQuantity = " + restInventoryQuantity);
    assertNotEquals(originInventoryQuantity, restInventoryQuantity);

  }

  @Test
  @DisplayName("[인벤토리 예약 객실 증가 동시성 테스트] 80개의 자리를 100명이 동시에 예약했을시 순차적으로 70개의 자리를 예약하고 30개의 실패를 발생시킨다.")
  void testConcurrentReservations() throws InterruptedException {
    // Given
    int numberOfThreads = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);  // 100 개의 서비스 동작
    CountDownLatch latch = new CountDownLatch(numberOfThreads); // 100개 쓰레드 동작을 기다려줌

    Long hotelId = 101L;
    LocalDate startDate = LocalDate.of(2025,2,1);
    LocalDate endDate = LocalDate.of(2025,2,2);

    int originInventoryQuantity = inventoryService.getInventoryQuantityByRoomType(
        101L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.STANDARD
    ).getQuantity();

    // When
    for(int i = 0; i < numberOfThreads; i++) {
      int finalI = i;
      executorService.submit(() -> {
        try {
          reservationService.createReservation(new ReservationForCreate(
              (long) finalI,
              hotelId,
              startDate,
              endDate,
              RoomType.STANDARD,
              ReservationStatus.PAID
          ));
        } catch (Exception e) {
          System.out.println(e + "::::::::::::::");
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    int restInventoryQuantity = inventoryService.getInventoryQuantityByRoomType(
        101L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.STANDARD
    ).getQuantity();


    // Then
    assertNotEquals(originInventoryQuantity, restInventoryQuantity);
    assertThat(restInventoryQuantity).isZero();

  }
}
