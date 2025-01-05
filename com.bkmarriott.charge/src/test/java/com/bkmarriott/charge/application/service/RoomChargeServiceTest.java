package com.bkmarriott.charge.application.service;

import com.bkmarriott.charge.application.exception.RoomChargeDuplicatedException;
import com.bkmarriott.charge.application.exception.RoomChargeErrorMessage;
import com.bkmarriott.charge.application.exception.RoomChargeNotFoundException;
import com.bkmarriott.charge.application.outputport.RoomChargeOutputPort;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] RoomChargeService Unit test")
class RoomChargeServiceTest {

    @InjectMocks
    private RoomChargeService roomChargeService;
    @Mock
    private RoomChargeOutputPort roomChargeOutputPort;

    @Test
    @DisplayName("[객실 요금 생성 성공 테스트] 객실 요금 생성 후 객실 요금 정보를 반환한다.")
    void create_successTest() {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;
        RoomChargeForCreate roomChargeForCreate = RoomChargeForCreate.of(hotelId, roomType, date, charge);

        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.empty());

        RoomCharge mockRoomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);
        Mockito.when(roomChargeOutputPort.create(ArgumentMatchers.any(RoomChargeForCreate.class)))
                .thenReturn(mockRoomCharge);

        // When
        RoomCharge result = roomChargeService.create(roomChargeForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(hotelId, result.getId().hotelId()),
                () -> Assertions.assertEquals(roomType, result.getId().roomType()),
                () -> Assertions.assertEquals(date, result.getId().date()),
                () -> Assertions.assertEquals(charge, result.getCharge())
        );
    }

    @Test
    @DisplayName("[객실 요금 생성 실패 테스트] 객실 요금이 중복되는 경우 예외를 발생시킨다.")
    void create_failTest_duplicate() {
        // Given
        RoomChargeForCreate roomChargeForCreate = RoomChargeForCreate.of(1L, RoomType.STANDARD, LocalDate.now(), 10000);

        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.of(Mockito.mock(RoomCharge.class)));

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> roomChargeService.create(roomChargeForCreate))
                        .isInstanceOf(RoomChargeDuplicatedException.class)
                        .hasMessage(RoomChargeErrorMessage.ROOM_TYPE_DUPLICATED.getMessage())
        );
    }

    @Test
    @DisplayName("[객실 요금 조회 성공 테스트] 해당 객실 타입의 객실 요금을 반환한다.")
    void find_successTest() {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer charge = 10000;
        RoomChargeId roomChargeId = RoomChargeId.of(hotelId, roomType, date);

        RoomCharge mockRoomCharge = new RoomCharge(roomChargeId, charge);
        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.of(mockRoomCharge));

        // When
        RoomCharge result = roomChargeService.findOne(roomChargeId);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(hotelId, result.getId().hotelId()),
                () -> Assertions.assertEquals(roomType, result.getId().roomType()),
                () -> Assertions.assertEquals(date, result.getId().date()),
                () -> Assertions.assertEquals(charge, result.getCharge())
        );
    }

    @Test
    @DisplayName("[객실 요금 조회 실패 테스트] 존재하지 않는 객실 타입인 경우 예외를 발생시킨다.")
    void find_failTest_invalidType() {
        // Given
        RoomChargeId roomChargeId = RoomChargeId.of(1L, RoomType.STANDARD, LocalDate.now());

        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.empty());

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> roomChargeService.findOne(roomChargeId))
                        .isInstanceOf(RoomChargeNotFoundException.class)
                        .hasMessage(RoomChargeErrorMessage.ROOM_CHARGE_NOT_EXIST.getMessage())
        );
    }

    @Test
    @DisplayName("[객실 요금 수정 성공 테스트] 객실 요금 수정 후 객실 요금 정보를 반환한다.")
    void update_successTest() {
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.of(2025, 1, 1);
        Integer newCharge = 10000;
        RoomChargeForCreate roomChargeForCreate = RoomChargeForCreate.of(hotelId, roomType, date, newCharge);

        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.of(Mockito.mock(RoomCharge.class)));

        RoomCharge newMockRoomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), newCharge);
        Mockito.when(roomChargeOutputPort.updateCharge(ArgumentMatchers.any(RoomCharge.class), ArgumentMatchers.any()))
                .thenReturn(newMockRoomCharge);

        // When
        RoomCharge result = roomChargeService.update(roomChargeForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(hotelId, result.getId().hotelId()),
                () -> Assertions.assertEquals(roomType, result.getId().roomType()),
                () -> Assertions.assertEquals(date, result.getId().date()),
                () -> Assertions.assertEquals(newCharge, result.getCharge())
        );
    }

    @Test
    @DisplayName("[객실 요금 수정 실패 테스트] 존재하지 않는 객실 타입인 경우 예외를 발생시킨다.")
    void update_failTest_invalidType() {
        // Given
        RoomChargeForCreate roomChargeForCreate = RoomChargeForCreate.of(1L, RoomType.STANDARD, LocalDate.now(), 1000);

        Mockito.when(roomChargeOutputPort.findById(ArgumentMatchers.any(RoomChargeId.class)))
                .thenReturn(Optional.empty());

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> roomChargeService.update(roomChargeForCreate))
                        .isInstanceOf(RoomChargeNotFoundException.class)
                        .hasMessage(RoomChargeErrorMessage.ROOM_CHARGE_NOT_EXIST.getMessage())
        );
    }

}