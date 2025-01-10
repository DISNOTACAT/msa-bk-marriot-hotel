package com.bkmarriott.charge.domain;

import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("[Domain] RoomCharge Unit test")
class RoomChargeTest {

    private final Long hotelId = 1L;
    private final RoomType roomType = RoomType.STANDARD;
    private final LocalDate date = LocalDate.now();
    private final Integer charge = 10000;

    @Test
    @DisplayName("[RoomCharge 생성 성공 테스트] RoomCharge 도메인 객체를 생성한다.")
    void createRoomCharge_successTest() {
        // Given & When
        RoomCharge roomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(hotelId, roomCharge.getId().hotelId()),
                () -> Assertions.assertEquals(roomType, roomCharge.getId().roomType()),
                () -> Assertions.assertEquals(date, roomCharge.getId().date()),
                () -> Assertions.assertEquals(charge, roomCharge.getCharge())
        );
    }

    @Test
    @DisplayName("[RoomCharge 수정 성공 테스트] RoomCharge 도메인 객체를 수정한다.")
    void updateRoomCharge_successTest() {
        // Given
        RoomCharge roomCharge = new RoomCharge(RoomChargeId.of(hotelId, roomType, date), charge);
        Integer newCharge = 20000;

        // When
        roomCharge.updateCharge(newCharge);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(hotelId, roomCharge.getId().hotelId()),
                () -> Assertions.assertEquals(roomType, roomCharge.getId().roomType()),
                () -> Assertions.assertEquals(date, roomCharge.getId().date()),
                () -> Assertions.assertEquals(newCharge, roomCharge.getCharge())
        );
    }
}