package com.bkmarriott.reservationservice.reservation.application.service;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;

import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryMessageSender;

import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;

import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryService;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
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

  @InjectMocks private InventoryService inventoryService;
  @Mock private InventoryCommandOutputPort inventoryCommandOutputPort;
  @Mock private ReservationQueryOutputPort reservationQueryOutputPort;
  @Mock private InventoryCacheOutputPort inventoryCacheOutputPort;
  @Mock private InventoryMessageSender inventoryMessageSender;

  @Test
  @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 성공 테스트] 예약 기간에 따라 가용 가능하다면 예외를 터뜨리지 않는다.")
  void prepareAvailableRoom_successTest(){
    // Given
    InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);
    int idx = 0;
    int totalInventory = 80;
    int[] totalReserved = new int[]{77, 40, 59};

        List<RoomInventoryEvent.RoomStockInfo> roomStockInfoList = List.of(
                new RoomInventoryEvent.RoomStockInfo(0L, "inventory:101:2025-02-01:DELUXE", (long) (totalInventory - totalReserved[idx++])),
                new RoomInventoryEvent.RoomStockInfo(1L, "inventory:101:2025-02-02:DELUXE", (long) (totalInventory - totalReserved[idx++])),
                new RoomInventoryEvent.RoomStockInfo(2L, "inventory:101:2025-02-03:DELUXE", (long) (totalInventory - totalReserved[idx]))

        );

        Mockito.when(inventoryCacheOutputPort.decreaseRoomCount(query)).thenReturn(roomStockInfoList);
        Mockito.doNothing().when(inventoryMessageSender).sendMessage(Mockito.any(RoomInventoryEvent.class));

        // When & Then
        Assertions.assertDoesNotThrow(() -> inventoryService.prepareAvailableRoom(query));
        Mockito.verify(inventoryCacheOutputPort, Mockito.times(1)).decreaseRoomCount(query);
    }

//    @Test
//    @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 실패 테스트] 예약정보에 해당하는 객실 정보가 없다면 예외 발생")
//    void prepareAvailableRoom_failTest(){
//        // Given
//        InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);
//
//        ArrayList<Inventory> inventories = new ArrayList<>();
//        Mockito.when(inventoryQueryOutputPort.findInventoryFromReservation(query)).thenReturn(inventories);
//
//        // When & Then
//        Assertions.assertAll(
//                () -> assertThatThrownBy(() -> inventoryService.prepareAvailableRoom(query))
//                        .isInstanceOf(ResourceNotFoundException.class)
//                        .hasMessage("해당 예약정보에 해당하는 객실 정보를 찾을 수 없습니다.")
//        );
//    }

  @Test
  @DisplayName("[인벤토리 예약 객실 증가 성공 테스트] PAID 예약 아이디가 주어졌을 시, 예약 기간 객실 수를 증가시킨 후 도메인 리스트를 반환한다.")
  void update_increase_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation mockReservation = testReservation();

    Inventory mock1 = Inventory.of(1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 300, 0);
    Inventory mock2 = Inventory.of(1L, LocalDate.of(2025, 2, 2), RoomType.DELUXE, 300, 0);

    Inventory increase1 = Inventory.of(mock1.getHotelId(), mock1.getDate(), mock1.getRoomType(), mock1.getTotalInventory(), mock1.getTotalReserved() + 1);
    Inventory increase2 = Inventory.of(mock2.getHotelId(), mock2.getDate(), mock2.getRoomType(), mock2.getTotalInventory(), mock2.getTotalReserved() + 1);

    Mockito.when(reservationQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(mockReservation));
    Mockito.when(inventoryCommandOutputPort.increaseReservedInventory(ArgumentMatchers.any(Reservation.class)))
        .thenReturn(List.of(increase1, increase2));

    //When
    List<Inventory> actual =  inventoryService.updateTotalReservedInventory(reservationId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(DAYS.between(mockReservation.getStartDate(), mockReservation.getEndDate().plusDays(1)), actual.size()),
        () -> Assertions.assertEquals(mock1.getHotelId(), actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(mock1.getDate(), actual.get(0).getDate()),
        () -> Assertions.assertEquals(mock1.getRoomType(), actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(mock1.getTotalInventory(), actual.get(0).getTotalInventory()),
        () -> Assertions.assertEquals(mock1.getTotalReserved() + 1, actual.get(0).getTotalReserved())
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

    Inventory mock1 = Inventory.of(1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 300, 0);
    Inventory mock2 = Inventory.of(1L, LocalDate.of(2025, 2, 2), RoomType.DELUXE, 300, 0);

    Inventory decrease1 = Inventory.of(mock1.getHotelId(), mock1.getDate(), mock1.getRoomType(), mock1.getTotalInventory(), mock1.getTotalReserved() - 1);
    Inventory decrease2 = Inventory.of(mock2.getHotelId(), mock2.getDate(), mock2.getRoomType(), mock2.getTotalInventory(), mock2.getTotalReserved() - 1);

    Mockito.when(reservationQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(mockReservation));
    Mockito.when(inventoryCommandOutputPort.decreaseReservedInventory(ArgumentMatchers.any(Reservation.class)))
        .thenReturn(List.of(decrease1, decrease2));

    //When
    List<Inventory> actual =  inventoryService.updateTotalReservedInventory(reservationId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(DAYS.between(mockReservation.getStartDate(), mockReservation.getEndDate().plusDays(1)), actual.size()),
        () -> Assertions.assertEquals(mock1.getHotelId(), actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(mock1.getDate(), actual.get(0).getDate()),
        () -> Assertions.assertEquals(mock1.getRoomType(), actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(mock1.getTotalInventory(), actual.get(0).getTotalInventory()),
        () -> Assertions.assertEquals(mock1.getTotalReserved() - 1, actual.get(0).getTotalReserved())
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

    Inventory mock1 = Inventory.of(1L, LocalDate.of(2025, 2, 1), RoomType.DELUXE, 300, 0);
    Inventory mock2 = Inventory.of(1L, LocalDate.of(2025, 2, 2), RoomType.DELUXE, 300, 0);

    Inventory decrease1 = Inventory.of(mock1.getHotelId(), mock1.getDate(), mock1.getRoomType(), mock1.getTotalInventory(), mock1.getTotalReserved() - 1);
    Inventory decrease2 = Inventory.of(mock2.getHotelId(), mock2.getDate(), mock2.getRoomType(), mock2.getTotalInventory(), mock2.getTotalReserved() - 1);

    Mockito.when(reservationQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(mockReservation));
    Mockito.when(inventoryCommandOutputPort.decreaseReservedInventory(ArgumentMatchers.any(Reservation.class)))
        .thenReturn(List.of(decrease1, decrease2));

    //When
    List<Inventory> actual =  inventoryService.updateTotalReservedInventory(reservationId);

    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(DAYS.between(mockReservation.getStartDate(), mockReservation.getEndDate().plusDays(1)), actual.size()),
        () -> Assertions.assertEquals(mock1.getHotelId(), actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(mock1.getDate(), actual.get(0).getDate()),
        () -> Assertions.assertEquals(mock1.getRoomType(), actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(mock1.getTotalInventory(), actual.get(0).getTotalInventory()),
        () -> Assertions.assertEquals(mock1.getTotalReserved() - 1, actual.get(0).getTotalReserved())
    );
  }


  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 존재하지 않는 예약 아이디가 주어진 경우 예외를 발생시킨다.")
  void update_FailureTest() {
    //Given
    Long reservationId = 1L;

    Mockito.when(reservationQueryOutputPort.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReservedInventory(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 예약 정보")
        );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 예기치 못한 예외 발생 시 InventoryUpdateFailureException 이 발생한다.")
  void update_failureTest_dueToUnexpectedError() {
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

    Mockito.when(reservationQueryOutputPort.findById(ArgumentMatchers.any(Long.class)))
        .thenReturn(Optional.of(mockReservation));

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReservedInventory(reservationId))
            .isInstanceOf(InventoryUpdateFailureException.class)
            .hasMessage("객실 예약 인벤토리 정보 수정 실패")
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 수정 실패 테스트] 예약이 주어졌지만 존재하지 않는 인벤토리의 경우 ResourceNotFoundException 가 발생한다.")
  void update_failureTest_dueToPAIDNotFound() {
    // Given
    Long reservationId = 1L;

    Mockito.when(reservationQueryOutputPort.findById(reservationId))
    .thenReturn(Optional.empty());

    // When & Then
    Assertions.assertAll(
        () -> assertThatThrownBy(() -> inventoryService.updateTotalReservedInventory(reservationId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재하지 않는 예약 정보")
    );
  }

  private Reservation testReservation() {

    Long reservationId = 1L;
    Long userId =  1L;
    Long hotelId =  101L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);
    RoomType roomType = RoomType.DELUXE;
    ReservationStatus status = ReservationStatus.PAID;
    Long roomId = null;

    return new Reservation(reservationId, userId, hotelId, startDate, endDate, roomType, status, roomId);
  }
}
