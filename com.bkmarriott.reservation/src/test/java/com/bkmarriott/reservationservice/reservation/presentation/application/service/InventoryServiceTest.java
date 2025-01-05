package com.bkmarriott.reservationservice.reservation.presentation.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.presentation.rest.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] InventoryService Unit test")
class InventoryServiceTest {

  @InjectMocks
  private InventoryService inventoryService;
  @Mock
  private InventoryCommandOutputPort inventoryCommandOutputPort;
  @Mock
  private ReservationQueryOutputPort reservationQueryOutputPort;

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 성공 테스트] PAID 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 증가시킨 후 도메인 리스트를 반환한다.")
  void update_increase_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = Reservation.builder()
        .reservationId(reservationId)
        .userId(1L)
        .hotelId(101L)
        .startDate(LocalDate.of(2025, 2, 1))
        .endDate(LocalDate.of(2025, 2, 2))
        .roomType(RoomType.DELUXE)
        .status(ReservationStatus.PAID)
        .build();

    Inventory mockInventory = Inventory.of(
    101L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 78);

    Inventory increase = Inventory.of(
        101L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 79);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));
    Mockito.mockStatic(Inventory.class)
      .when(() -> Inventory.from(mockReservation))
          .thenReturn(List.of(mockInventory));
    Mockito.when(inventoryCommandOutputPort.increaseReserved(Mockito.any(Inventory.class)))
        .thenReturn(Optional.of(increase));

    //When
    List<Inventory> actual =  inventoryService.updateTotalReserved(reservationId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(actual.size(), 1),
        () -> Assertions.assertEquals(actual.get(0).getHotelId(), mockInventory.getHotelId()),
        () -> Assertions.assertEquals(actual.get(0).getDate(), mockInventory.getDate()),
        () -> Assertions.assertEquals(actual.get(0).getRoomType(), mockInventory.getRoomType()),
        () -> Assertions.assertEquals(actual.get(0).getTotalInventory(), mockInventory.getTotalInventory()),
        () -> Assertions.assertEquals(actual.get(0).getTotalReserved(), mockInventory.getTotalReserved() + 1)
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 성공 테스트] CANCELLED 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 감소시킨 후 도메인 리스트를 반환한다.")
  void update_decrease_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = Reservation.builder()
        .reservationId(reservationId)
        .userId(1L)
        .hotelId(101L)
        .startDate(LocalDate.of(2025, 2, 1))
        .endDate(LocalDate.of(2025, 2, 2))
        .roomType(RoomType.DELUXE)
        .status(ReservationStatus.CANCELLED)
        .build();

    Inventory mockInventory = Inventory.of(
        101L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 78);

    Inventory decrease = Inventory.of(
        101L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 77);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));
    Mockito.mockStatic(Inventory.class)
        .when(() -> Inventory.from(mockReservation))
        .thenReturn(List.of(mockInventory));
    Mockito.when(inventoryCommandOutputPort.decreaseReserved(Mockito.any(Inventory.class)))
        .thenReturn(Optional.of(decrease));

    //When
    List<Inventory> actual =  inventoryService.updateTotalReserved(reservationId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(actual.size(), 1),
        () -> Assertions.assertEquals(actual.get(0).getHotelId(), mockInventory.getHotelId()),
        () -> Assertions.assertEquals(actual.get(0).getDate(), mockInventory.getDate()),
        () -> Assertions.assertEquals(actual.get(0).getRoomType(), mockInventory.getRoomType()),
        () -> Assertions.assertEquals(actual.get(0).getTotalInventory(), mockInventory.getTotalInventory()),
        () -> Assertions.assertEquals(actual.get(0).getTotalReserved(), mockInventory.getTotalReserved() - 1)
    );
  }


  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 존재하지 않는 예약 아이디가 주어진 경우 예외를 발생시킨다.")
  void update_FailureTest() {
    //Given
    Long reservationId = 101L;

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 예약 정보")
        );
  }
}
