package com.bkmarriott.hotel.infrastructure.feignClient.adapter;

import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Infrastructure] ChargeFeignClient Unit Test")
public class ChargeFeignClientAdapterTest {

    @InjectMocks private ChargeFeignClientAdapter chargeFeignClientAdapter;
    @Mock ChargeClient chargeClient;

    @Test
    @DisplayName("[요금 조회 요청 성공 테스트] 호텔 ID 리스트, 날짜, 객실 타입을 입력하면 해당 날짜와 객실 타입에 맞는 호텔 ID의 요금 정보를 반환한다.")
    public void getRoomCharge_success() throws Exception {
        // Given
        List<Long> hotelIds = List.of(1L, 2L, 3L, 4L, 5L);
        RoomType roomType = RoomType.STANDARD;
        LocalDate date = LocalDate.parse("2025-01-04");
        int expectedRoomCharge = 100000;

        List<RoomChargeResponse> mockRoomCharge = new ArrayList<>();
        for(Long hotelId : hotelIds){
            mockRoomCharge.add(new RoomChargeResponse(hotelId, roomType, expectedRoomCharge, date));
        }

        Mockito.when(chargeClient.getRoomCharge(hotelIds, roomType, date)).thenReturn(mockRoomCharge);
        
        // When
        List<RoomChargeResponse> charges = chargeFeignClientAdapter.getRoomCharge(hotelIds, date);

        // Then
        for (int i = 0; i < charges.size(); i++) {
            RoomChargeResponse actual = charges.get(i);
            RoomChargeResponse expected = mockRoomCharge.get(i);

            assertThat(actual.hotelId()).isEqualTo(expected.hotelId());
            assertThat(actual.roomType()).isEqualTo(expected.roomType());
            assertThat(actual.charge()).isEqualTo(expected.charge());
            assertThat(actual.date()).isEqualTo(expected.date());
        }

        List<Long> resultHotelIds = charges.stream()
                .map(RoomChargeResponse::hotelId)
                .collect(Collectors.toList());

        assertThat(resultHotelIds).containsExactlyElementsOf(hotelIds);
    }

    @Test
    @DisplayName("[요금 조회 실패 시 Fallback 테스트] ChargeClient 예외 발생 시 Fallback 메서드를 통해 빈 리스트를 반환한다.")
    public void getRoomCharge_fallback_success() throws Exception {
        // Given
        List<Long> hotelIds = List.of(1L, 2L, 3L, 4L, 5L);
        LocalDate date = LocalDate.parse("2025-01-04");

        String errorMessage = "FeignClient Exception";
        Throwable throwable = new RuntimeException(errorMessage);

        // When
        List<RoomChargeResponse> fallbackCharge = chargeFeignClientAdapter.fallbackGetRoomCharge(hotelIds, date, throwable);

        // Then
        Assertions.assertAll(
                () -> assertTrue(fallbackCharge.isEmpty()),
                () -> assertThat(throwable).isInstanceOf(RuntimeException.class), // 예외 타입 확인
                () -> assertThat(throwable).hasMessageContaining(errorMessage)
        );
    }
}
