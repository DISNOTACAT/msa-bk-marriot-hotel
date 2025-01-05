package com.bkmarriott.charge.infrastructure.persistence.adapter;

import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import com.bkmarriott.charge.infrastructure.persistence.config.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

@DisplayName("[Infrastructure] RoomCharge Repository Unit Test")
@RepositoryTest
class RoomChargeAdapterTest {

    @Autowired
    private RoomChargeAdapter roomChargeAdapter;

    @Test
    @DisplayName("[객실 요금 등록 성공 테스트] 객실 요금을 등록한 뒤 도메인 객체를 반환한다.")
    void create_successTest() {
        // Given
        RoomChargeForCreate roomChargeForCreate = genRoomChargeForCreate();
        // When
        RoomCharge actual = roomChargeAdapter.create(roomChargeForCreate);
        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(roomChargeForCreate.id().hotelId(), actual.getId().hotelId()),
                () -> Assertions.assertEquals(roomChargeForCreate.id().roomType(), actual.getId().roomType()),
                () -> Assertions.assertEquals(roomChargeForCreate.id().date(), actual.getId().date()),
                () -> Assertions.assertEquals(roomChargeForCreate.charge(), actual.getCharge())
        );
    }

    private RoomChargeForCreate genRoomChargeForCreate() {
        Long hotelId = 1L;
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.now();
        Integer charge = 10000;

        return RoomChargeForCreate.of(hotelId, roomType, date, charge);
    }

    @Test
    @DisplayName("[객실 요금 조회 성공 테스트] 호텔 아이디, 객실 타입으로 객실 요금을 조회한 뒤 Optional 객체에 담아 반환한다.")
    void findById_successTest() {
        // Given
        RoomChargeForCreate roomChargeForCreate = genRoomChargeForCreate();
        RoomCharge roomCharge = roomChargeAdapter.create(roomChargeForCreate);
        // When
        Optional<RoomCharge> optionalRoomCharge = roomChargeAdapter.findById(roomCharge.getId());
        // Then
        Assertions.assertAll(
                () -> Assertions.assertTrue(optionalRoomCharge.isPresent()),
                () -> Assertions.assertEquals(roomCharge.getId(), optionalRoomCharge.get().getId())
        );
    }

    @Test
    @DisplayName("[객실 요금 수정 성공 테스트] 객실 요금을 수정한 뒤 도메인 객체를 반환한다.")
    void updateCharge_successTest() {
        // Given
        RoomCharge roomCharge = new RoomCharge(RoomChargeId.of(1L, RoomType.STANDARD, LocalDate.now()), 10000);
        Integer updateCharge = 50000;
        // When
        RoomCharge actual = roomChargeAdapter.updateCharge(roomCharge, updateCharge);
        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(roomCharge.getId().hotelId(), actual.getId().hotelId()),
                () -> Assertions.assertEquals(roomCharge.getId().roomType(), actual.getId().roomType()),
                () -> Assertions.assertEquals(roomCharge.getId().date(), actual.getId().date()),
                () -> Assertions.assertEquals(roomCharge.getCharge(), actual.getCharge())
        );
    }
}