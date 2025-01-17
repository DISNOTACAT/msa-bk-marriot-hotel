package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config.RepositoryTest;
import java.time.LocalDate;
import java.util.List;
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
  private InventoryQueryDslRepository inventoryQueryDslRepository;


  @Test
  @DisplayName("[인벤토리 예약 객실 증가 성공 테스트] 인벤토리 도메인이 주어졌을 시, 예약 객실 수를 증가시킨 후 도메인 객체를 반환한다.")
  void update_increase_successTest() {

    //Given
    Reservation reservation = testReservation();
    //When
    List<Inventory> actual = inventoryCommandAdaptor.increaseReservedInventory(reservation);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(reservation.getHotelId(), actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(reservation.getStartDate(), actual.get(0).getDate()),
        () -> Assertions.assertEquals(reservation.getRoomType(), actual.get(0).getRoomType())
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 객실 감소 성공 테스트] 인벤토리 도메인이 주어졌을 시, 예약 객실 수를 감소시킨 후 도메인 객체를 반환한다.")
  void update_decrease_successTest() {

    //Given
    Reservation reservation = testReservation();
    //When
    List<Inventory> actual = inventoryCommandAdaptor.decreaseReservedInventory(reservation);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(reservation.getHotelId(), actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(reservation.getStartDate(), actual.get(0).getDate()),
        () -> Assertions.assertEquals(reservation.getRoomType(), actual.get(0).getRoomType())
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
