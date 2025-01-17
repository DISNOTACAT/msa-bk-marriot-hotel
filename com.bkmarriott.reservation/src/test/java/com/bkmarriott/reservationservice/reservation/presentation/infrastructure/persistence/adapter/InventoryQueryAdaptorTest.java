package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryRepository;
import com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config.RepositoryTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] Inventory Repository Unit Test")
@RepositoryTest
class InventoryQueryAdaptorTest {

  @Autowired
  private InventoryRepository inventoryRepository;
  @Autowired
  private InventoryQueryDslRepository inventoryQueryDslRepository;


  @Test
  @DisplayName("[인벤토리 조회 성공 테스트] 호텔 아이디, 룸 타입, 날짜로 인벤토리를 조회한 후, 엔티티 객체를 반환한다.")
  void find_successTest() {
    //Given
    Long hotelId = 101L;
    LocalDate date = LocalDate.of(2025, 2, 1);
    RoomType roomType = RoomType.DELUXE;
    //When
    Optional<Inventory> actual = inventoryRepository.findById(
        RoomTypeInventoryId.of(hotelId, date, roomType))
        .map(RoomTypeInventoryEntity::toDomain);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(hotelId, actual.get().getHotelId()),
        () -> Assertions.assertEquals(date, actual.get().getDate()),
        () -> Assertions.assertEquals(roomType, actual.get().getRoomType())
    );
  }

  @Test
  @DisplayName("[인벤토리 예약 가능 객실 수 조회 성공 테스트] 호텔 아이디와 숙박 일자가 주어졌을 시, 예약 가능한 타입별 객실 수를 반환한다.")
  void find_availble_quantity_successTest() {

    InventoryDateQuery query = new InventoryDateQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"));

    List<AvailableRoomCountDto> mockResponse = List.of(
        new AvailableRoomCountDto(RoomEntityType.DELUXE, 300),
        new AvailableRoomCountDto(RoomEntityType.STANDARD, 70),
        new AvailableRoomCountDto(RoomEntityType.SUITE, 60),
        new AvailableRoomCountDto(RoomEntityType.TWIN, 50)
    );

    // When
    List<AvailableRoomCountDto> actual = inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(
        query);

    // Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mockResponse.get(0).getRoomType(), actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(mockResponse.get(0).getCount(), actual.get(0).getCount())
    );
  }

  @Test
  @DisplayName("[인벤토리 조회 성공 테스트] 예약 정보가 주어질 경우 인벤토리 엔티티를 반환한다.")
  void find_RoomTypeInventoryEntity_successTest() {
    // Given
    InventoryQuery query = new InventoryQuery(101L, LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2), RoomType.DELUXE);

    Inventory inventory1 = Inventory.of(101L, LocalDate.of(2025, 2, 1),
        RoomType.DELUXE, 300, 0);
    Inventory inventory2 = Inventory.of(101L, LocalDate.of(2025, 2, 2),
        RoomType.DELUXE, 300, 0);
    List<Inventory> mockResponse = List.of(inventory1, inventory2);

    // When
    List<Inventory> actual = inventoryQueryDslRepository.findInventoryFromReservation(
        query).stream().map(RoomTypeInventoryEntity::toDomain).toList();

    // Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(mockResponse.get(0).getHotelId(),
            actual.get(0).getHotelId()),
        () -> Assertions.assertEquals(mockResponse.get(0).getRoomType(),
            actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(mockResponse.get(0).getDate(),
            actual.get(0).getDate()),
        () -> Assertions.assertEquals(mockResponse.get(0).getTotalInventory(),
            actual.get(0).getTotalInventory()),
        () -> Assertions.assertEquals(mockResponse.get(0).getTotalReserved(),
            actual.get(0).getTotalReserved())
    );
  }

  @Test
  @DisplayName("[인벤토리 조회 성공 테스트] 숙박 조회 기간이 주어질 경우 해당 기간의 가용 객실 정보를 반환한다.")
  void findInventoryFromReservation_successTest() {

    InventoryDateQuery query = new InventoryDateQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"));

    // When
    List<AvailableRoomCountDto> actual = inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(query);

    // Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(RoomType.DELUXE, actual.get(0).getRoomType()),
        () -> Assertions.assertEquals(300, actual.get(0).getCount())
    );
  }
}
