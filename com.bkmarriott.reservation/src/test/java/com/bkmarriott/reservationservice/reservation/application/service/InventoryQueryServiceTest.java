package com.bkmarriott.reservationservice.reservation.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventorySearchRequestDto;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.service.inventory.InventoryQueryService;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response.InventorySearchResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
class InventoryQueryServiceTest {
    @InjectMocks private InventoryQueryService inventoryService;
    @Mock private InventoryQueryOutputPort inventoryQueryOutputPort;
    @Mock private ChargeOutputPort chargeOutputPort;


    @Test
    @DisplayName("[객실 수와 요금 반환 성공 테스트] 요청 기간의 객실 타입별 예약 가능 수와 금액을 반환한다.")
    void get_InventoryQuantity_successTest(){
        // Given
        int charge = 100000;
        InventorySearchRequestDto request = new InventorySearchRequestDto(1L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"));

        AvailableRoomCountDto room1 = new AvailableRoomCountDto(RoomEntityType.STANDARD, 5);
        AvailableRoomCountDto room2 = new AvailableRoomCountDto(RoomEntityType.DELUXE, 3);
        List<AvailableRoomCountDto> availableRooms = Arrays.asList(room1, room2);

        Mockito.when(inventoryQueryOutputPort.findAvailableRoomTypeAndCount(
            any())).thenReturn(availableRooms);
        Mockito.when(chargeOutputPort.findRoomChargeByDates(any(InventoryQuery.class)))
            .thenReturn(charge);

        // When
        List<InventorySearchResponse> actual = inventoryService.getAvailableRoomsWithCharge(request);

        Assertions.assertAll(
            () -> Assertions.assertEquals(2, actual.size()),
            () -> Assertions.assertEquals(room1.getRoomType(), actual.get(0).roomType()),
            () -> Assertions.assertEquals(charge, actual.get(0).charge())
        );

    }

    @Test
    @DisplayName("[인벤토리 예약 가능 객실 수 조회 실패 테스트] 존재하지 않는 호텔 아이디 또는 일자가 주어진 경우 예외를 발생시킨다.")
    void get_inventory_quantity_FailureTest() {
        InventorySearchRequestDto request = new InventorySearchRequestDto(11L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"));

        Mockito.when(
                inventoryQueryOutputPort.findAvailableRoomTypeAndCount(
                    ArgumentMatchers.any(InventoryDateQuery.class)))
            .thenReturn(null);

        // Given & When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(
                () -> inventoryService.getAvailableRoomsWithCharge(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 가능 객실 수량 조회 결과가 없습니다.")
        );
    }

    @Test
    @DisplayName("[인벤토리 예약 가능 객실 수 조회 실패 테스트] 조회된 객실이 없는 경우 예외를 발생시킨다.")
    void get_inventory_quantity_isEmpty_FailureTest() {
        InventorySearchRequestDto request = new InventorySearchRequestDto(1L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"));

        Mockito.when(
                inventoryQueryOutputPort.findAvailableRoomTypeAndCount(
                    ArgumentMatchers.any(InventoryDateQuery.class)))
            .thenReturn(List.of());

        // Given & When & Then
        Assertions.assertAll(
            () -> assertThatThrownBy(
                () -> inventoryService.getAvailableRoomsWithCharge(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 가능 객실 수량 조회 결과가 없습니다.")
        );
    }
}
