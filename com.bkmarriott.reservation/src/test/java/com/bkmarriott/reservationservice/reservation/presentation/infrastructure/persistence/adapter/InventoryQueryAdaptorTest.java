package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
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

    Long hotelId = 101L;
    LocalDate startDate = LocalDate.of(2025, 2, 1);
    LocalDate endDate = LocalDate.of(2025, 2, 2);

    InventoryQueryRequestDto requestDto = new InventoryQueryRequestDto(hotelId, startDate, endDate);

    List<InventoryQueryResponseDto> mockResponse = List.of(
        new InventoryQueryResponseDto(RoomEntityType.DELUXE, 2),
        new InventoryQueryResponseDto(RoomEntityType.STANDARD, 44),
        new InventoryQueryResponseDto(RoomEntityType.TWIN, 33)
    );

    // When
    List<InventoryQueryResponseDto> actual = inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(
        requestDto);

    // Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(actual.get(0).getRoomType(), mockResponse.get(0).getRoomType()),
        () -> Assertions.assertEquals(actual.get(0).getQuantity(), mockResponse.get(0).getQuantity())
    );
  }

}
