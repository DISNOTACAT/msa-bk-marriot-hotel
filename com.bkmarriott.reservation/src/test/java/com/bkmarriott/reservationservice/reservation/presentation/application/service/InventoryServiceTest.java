package com.bkmarriott.reservationservice.reservation.presentation.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.service.InventoryService;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import java.time.LocalDate;
import java.util.List;
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
@DisplayName("[Application] InventoryService Unit test")
class InventoryServiceTest {

  @InjectMocks
  private InventoryService inventoryService;
  @Mock
  private InventoryCommandOutputPort inventoryCommandOutputPort;
  @Mock
  private ReservationQueryOutputPort reservationQueryOutputPort;
  @Mock
  private InventoryQueryOutputPort inventoryQueryOutputPort;

  @Test
  @DisplayName("[인벤토리 예약 객실 증가 성공 테스트] PAID 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 증가시킨 후 도메인 리스트를 반환한다.")
  void update_increase_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = new Reservation(
        reservationId,
        1L,
        1L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.DELUXE,
        ReservationStatus.PAID,
        null
    );

    Inventory mockInventory = Inventory.of(
    1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 78);

    Inventory increase = Inventory.of(
        1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 79);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));

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
  @DisplayName("[인벤토리 예약 객실 감소 성공 테스트] CANCELLED 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 감소시킨 후 도메인 리스트를 반환한다.")
  void update_decrease_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = new Reservation(
        reservationId,
        1L,
        1L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.DELUXE,
        ReservationStatus.CANCELLED,
        null
    );

    Inventory mockInventory = Inventory.of(
        1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 78);

    Inventory decrease = Inventory.of(
        1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 77);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));
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
  @DisplayName("[인벤토리 예약 객실 감소 성공 테스트] REFUNDED 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 감소시킨 후 도메인 리스트를 반환한다.")
  void update_decrease_refunded_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = new Reservation(
        reservationId,
        1L,
        1L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.DELUXE,
        ReservationStatus.REFUNDED,
        null
    );

    Inventory mockInventory = Inventory.of(
        1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 78);

    Inventory decrease = Inventory.of(
        1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 80, 77);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));
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
    Long reservationId = 1L;

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 예약 정보")
        );
  }


  @Test
  @DisplayName("[인벤토리 예약 가능 객실 수 조회 성공 테스트] 호텔 아이디와 숙박 일자가 주어졌을 시, 예약 가능한 타입별 객실 수를 반환한다.")
  void get_inventory_quantity_SuccessTest() {

    Long hotelId = 1L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);

    InventoryQueryRequestDto requestDto = new InventoryQueryRequestDto(hotelId, startDate, endDate);

    List<InventoryQueryResponseDto> mockResponse = List.of(
        new InventoryQueryResponseDto(RoomEntityType.DELUXE, 2),
        new InventoryQueryResponseDto(RoomEntityType.STANDARD, 44),
        new InventoryQueryResponseDto(RoomEntityType.TWIN, 33)
    );
    Mockito.when(inventoryQueryOutputPort.findAvailableRoomsByHotelIdAndDateRange(requestDto)
        ).thenReturn(mockResponse);

    // When
    List<InventoryQueryResponseDto> actual = inventoryQueryOutputPort.findAvailableRoomsByHotelIdAndDateRange(
        requestDto);

    // Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(actual.get(0).getRoomType(), mockResponse.get(0).getRoomType()),
        () -> Assertions.assertEquals(actual.get(0).getQuantity(), mockResponse.get(0).getQuantity())
        );
  }

  @Test
  @DisplayName("[인벤토리 예약 가능 객실 수 조회 실패 테스트] 존재하지 않는 호텔 아이디 또는 일자가 주어진 경우 예외를 발생시킨다.")
  void get_inventory_quantity_FailureTest() {
    Long hotelId = 11L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);

    Mockito.when(
            inventoryQueryOutputPort.findAvailableRoomsByHotelIdAndDateRange(
                    ArgumentMatchers.any(InventoryQueryRequestDto.class)))
        .thenReturn(null);

    // Given & When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(
            () -> inventoryService.getInventoryQuantity(hotelId, startDate, endDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 객실 수량 조회 결과가 없습니다.")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 가능 객실 수 조회 실패 테스트] 조회된 객실이 없는 경우 예외를 발생시킨다.")
  void get_inventory_quantity_isEmpty_FailureTest() {
    Long hotelId = 11L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);

    Mockito.when(
            inventoryQueryOutputPort.findAvailableRoomsByHotelIdAndDateRange(
                ArgumentMatchers.any(InventoryQueryRequestDto.class)))
        .thenReturn(List.of());

    // Given & When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(
            () -> inventoryService.getInventoryQuantity(hotelId, startDate, endDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 객실 수량 조회 결과가 없습니다.")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 예기치 못한 예외 발생 시 InventoryUpdateFailureException이 발생한다.")
  void update_increase_failureTest_dueToUnexpectedError() {
    // Given
    Long reservationId = 1L;
    Reservation mockReservation = new Reservation(
        reservationId,
        1L,
        1L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.DELUXE,
        ReservationStatus.PAID,
        null
    );

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));

    Mockito.when(inventoryCommandOutputPort.increaseReserved(Mockito.any(Inventory.class)))
        .thenThrow(new InventoryUpdateFailureException("객실 예약 인벤토리 정보 수정 실패"));

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(InventoryUpdateFailureException.class)
            .hasMessage("객실 예약 인벤토리 정보 수정 실패")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 업데이트 조건이 아닌 경우 InventoryUpdateFailureException이 발생한다.")
  void update__failureTest_dueToUnexpectedError() {
    // Given
    Long reservationId = 1L;
    Reservation mockReservation = new Reservation(
        reservationId,
        1L,
        1L,
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        RoomType.DELUXE,
        ReservationStatus.PENDING,
        null
    );

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(mockReservation));

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(InventoryUpdateFailureException.class)
            .hasMessage("객실 예약 인벤토리 정보 수정 실패")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] PAID 예약이 주어졌지만 존재하지 않는 인벤토리의 경우 ResourceNotFoundException가 발생한다.")
  void update__failureTest_dueToPAIDNotFound() {
    // Given
    Long reservationId = 1L;
    Reservation reservation = new Reservation(reservationId, 1L, 1L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2), RoomType.DELUXE, ReservationStatus.PAID, null);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
    .thenReturn(Optional.of(reservation));

    Mockito.when(inventoryCommandOutputPort.increaseReserved(ArgumentMatchers.any(Inventory.class)))
        .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 인벤토리 정보")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] CANCELLED 예약이 주어졌지만 존재하지 않는 인벤토리의 경우 ResourceNotFoundException가 발생한다.")
  void update__failureTest_dueToCANCELLEDNotFound() {
    // Given
    Long reservationId = 1L;
    Reservation reservation = new Reservation(reservationId, 1L, 1L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2), RoomType.DELUXE, ReservationStatus.CANCELLED, null);

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
        .thenReturn(Optional.of(reservation));

    Mockito.when(inventoryCommandOutputPort.decreaseReserved(ArgumentMatchers.any(Inventory.class)))
        .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReserved(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 인벤토리 정보")
    );
  }
}
