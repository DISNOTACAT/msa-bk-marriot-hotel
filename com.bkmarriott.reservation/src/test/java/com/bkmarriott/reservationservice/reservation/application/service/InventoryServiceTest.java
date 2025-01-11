package com.bkmarriott.reservationservice.reservation.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @InjectMocks private InventoryService inventoryService;
    @Mock private InventoryQueryOutputPort inventoryQueryOutputPort;
    @Mock private ChargeOutputPort chargeOutputPort;

    @Mock private InventoryCacheOutputPort inventoryCacheOutputPort;

    @Test
    @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 성공 테스트] 예약 기간에 따라 가용 가능하다면 예외를 터뜨리지 않는다.")
    void prepareAvailableRoom_successTest(){
        // Given
        InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);
        int idx = 0;
        int totalInventory = 80;
        int[] totalReserved = new int[]{77, 40, 59};

        int minAvailableCnt = totalInventory;

        ArrayList<Inventory> inventories = new ArrayList<>();
        List<LocalDate> localDates = query.startDate().datesUntil(query.endDate()).toList();
        for(LocalDate date : localDates){
            minAvailableCnt = Math.min(minAvailableCnt, totalInventory-totalReserved[idx]);
            inventories.add(Inventory.of(query.hotelId(), date, query.roomType(), 80, totalReserved[idx++]));
        }

        Mockito.when(inventoryQueryOutputPort.findInventoryFromReservation(query)).thenReturn(inventories);
        Mockito.doNothing().when(inventoryCacheOutputPort).decreaseRoomCount(query);

        // When & Then
        Assertions.assertDoesNotThrow(() -> inventoryService.prepareAvailableRoom(query));
        Mockito.verify(inventoryQueryOutputPort, Mockito.times(1)).findInventoryFromReservation(query);
        Mockito.verify(inventoryCacheOutputPort, Mockito.times(1)).decreaseRoomCount(query);
    }

    @Test
    @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 실패 테스트] 예약정보에 해당하는 객실 정보가 없다면 예외 발생")
    void prepareAvailableRoom_failTest(){
        // Given
        InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);

        ArrayList<Inventory> inventories = new ArrayList<>();
        Mockito.when(inventoryQueryOutputPort.findInventoryFromReservation(query)).thenReturn(inventories);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> inventoryService.prepareAvailableRoom(query))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("해당 예약정보에 해당하는 객실 정보를 찾을 수 없습니다.")
        );
    }

    @Test
    @DisplayName("[객실 수와 요금 반환 성공 테스트] 요청 기간의 객실 타입별 예약 가능 수와 금액을 반환한다.")
    void get_InventoryQuantity_successTest(){
        // Given
        int charge = 100000;
        Long hotelId = 1L;
        LocalDate startDate = LocalDate.parse("2025-02-01");
        LocalDate endDate = LocalDate.parse("2025-02-02");

        InventoryQueryResponseDto room1 = new InventoryQueryResponseDto(RoomEntityType.STANDARD, 5);
        InventoryQueryResponseDto room2 = new InventoryQueryResponseDto(RoomEntityType.DELUXE, 3);
        List<InventoryQueryResponseDto> availableRooms = Arrays.asList(room1, room2);

        Mockito.when(inventoryQueryOutputPort.findAvailableRoomsByHotelIdAndDateRange(
            any())).thenReturn(availableRooms);
        Mockito.when(chargeOutputPort.getRoomCharge(
            anyLong(), any(RoomType.class), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(charge);

        // When
        List<Response> actual = inventoryService.getInventoryQuantity(hotelId, startDate, endDate);

        Assertions.assertAll(
            () -> Assertions.assertEquals(2, actual.size()),
            () -> Assertions.assertEquals(room1.getRoomType(), actual.get(0).getRoomType()),
            () -> Assertions.assertEquals(charge, actual.get(0).getCharge())
        );

    }

}
