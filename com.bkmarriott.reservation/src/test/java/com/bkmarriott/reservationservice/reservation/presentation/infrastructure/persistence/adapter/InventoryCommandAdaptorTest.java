package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config.RepositoryTest;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] Inventory Repository Unit Test")
@RepositoryTest
class InventoryCommandAdaptorTest {

  @Autowired
  private InventoryCommandAdaptor inventoryCommandAdaptor;
  @Autowired
  private InventoryQueryAdaptor inventoryQueryAdaptor;


  @Test
  @DisplayName("[인벤토리 조회 성공 테스트] 호텔 아이디, 룸 타입, 날짜로 인벤토리를 조회한 후, 엔티티 객체를 반환한다.")
  void find_successTest() {
    //Given
    Long hotelId = 101L;
    LocalDate date = LocalDate.of(2025, 2, 1);
    RoomType roomType = RoomType.DELUXE;
    //When
    Optional<Inventory> actual = inventoryQueryAdaptor.findById(hotelId, date, roomType);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(hotelId, actual.get().getHotelId()),
        () -> Assertions.assertEquals(date, actual.get().getDate()),
        () -> Assertions.assertEquals(roomType, actual.get().getRoomType())
    );

  }


  @Test
  @DisplayName("[인벤토리 예약 객실 증가 성공 테스트] 인벤토리 도메인이 주어졌을 시, 예약 객실 수를 증가시킨 후 도메인 객체를 반환한다.")
  void update_increase_successTest() {

    //Given
    Inventory inventory = testInventory();
    //When
    Optional<Inventory> actual = inventoryCommandAdaptor.increaseReserved(inventory);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(inventory.getHotelId(), actual.get().getHotelId()),
        () -> Assertions.assertEquals(inventory.getDate(), actual.get().getDate()),
        () -> Assertions.assertEquals(inventory.getRoomType(), actual.get().getRoomType()),
        () -> Assertions.assertEquals(inventory.getTotalInventory(), actual.get().getTotalInventory()),
        () -> Assertions.assertEquals(inventory.getTotalReserved() + 1, actual.get().getTotalReserved())
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 감소 성공 테스트] 인벤토리 도메인이 주어졌을 시, 예약 객실 수를 감소시킨 후 도메인 객체를 반환한다.")
  void update_decrease_successTest() {

    //Given
    Inventory inventory = testInventory();
    //When
    Optional<Inventory> actual = inventoryCommandAdaptor.decreaseReserved(inventory);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(inventory.getHotelId(), actual.get().getHotelId()),
        () -> Assertions.assertEquals(inventory.getDate(), actual.get().getDate()),
        () -> Assertions.assertEquals(inventory.getRoomType(), actual.get().getRoomType()),
        () -> Assertions.assertEquals(inventory.getTotalInventory(), actual.get().getTotalInventory()),
        () -> Assertions.assertEquals(inventory.getTotalReserved() - 1, actual.get().getTotalReserved())
    );
  }

  private Inventory testInventory() {
    Long hotelId = 101L;
    LocalDate date = LocalDate.of(2025, 2, 1);
    RoomType roomType = RoomType.DELUXE;
    int totalInventory = 300;
    int totalReserved = 0;

    return Inventory.of(hotelId, date, roomType, totalInventory, totalReserved);
  }
}
