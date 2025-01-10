package com.bkmarriott.charge.infrastructure.persistence.adapter;

import com.bkmarriott.charge.domain.HotelType;
import com.bkmarriott.charge.domain.vo.RoomType;
import com.bkmarriott.charge.infrastructure.persistence.config.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayName("[Infrastructure] HotelType Repository Unit test")
@RepositoryTest
class HotelTypeAdapterTest {

    @Autowired
    private HotelTypeAdapter hotelTypeAdapter;

    @Test
    @DisplayName("[호텔 타입 전체 조회 성공 테스트] 전체 호텔 타입을 조회한 뒤 도메인 객체 리스트를 반환한다.")
    void findAll_successTest() {
        // When
        List<HotelType> result = hotelTypeAdapter.findAll();

        // Then
        Assertions.assertEquals(4, result.size());

        Assertions.assertAll("반환된 호텔 타입의 내용이 일치해야 합니다.",
                () -> assertHotelType(result.get(0), 1L, RoomType.STANDARD),
                () -> assertHotelType(result.get(1), 1L, RoomType.DELUXE)
        );
    }

    private void assertHotelType(HotelType hotelType, Long expectedHotelId, RoomType expectedRoomType) {
        Assertions.assertEquals(expectedHotelId, hotelType.getHotelId());
        Assertions.assertEquals(expectedRoomType, hotelType.getRoomType());
    }
}