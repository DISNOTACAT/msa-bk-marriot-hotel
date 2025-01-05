package com.bkmarriott.hotel.infrastructure.feignClient.adapter;

import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Infrastructure] ChargeFeignClient Unit Test")
public class ChargeFeignClientAdapterTest {

    @InjectMocks private ChargeFeignClientAdapter chargeFeignClientAdapter;
    @Mock ChargeClient chargeClient;

    @Test
    @DisplayName("[요금 조회 요청 성공 테스트] 전달받은 호텔과 객실 타입의 체크인 날짜에 해당하는 금액을 반환한다.")
    public void getRoomCharge_success() throws Exception {
        // Given
        Hotel hotel = new Hotel(1L,"Marriott Hotel Seoul","South Korea", "Seoul", "address", "description");
        LocalDate date = LocalDate.parse("2025-01-04");

        int expectedRoomCharge = 100000;
        RoomChargeResponse roomCharge = new RoomChargeResponse(1L, RoomType.STANDARD, expectedRoomCharge, date);
        Mockito.when(chargeClient.getRoomCharge(hotel.getHotelId(), RoomType.STANDARD, date)).thenReturn(roomCharge);
        
        // When
        int charge = chargeFeignClientAdapter.getRoomCharge(hotel, date);

        // Then
        assertEquals(expectedRoomCharge, charge);
    }

    @Test
    @DisplayName("[요금 조회 실패 시 Fallback 테스트] ChargeClient 예외 발생 시 Fallback 메서드를 통해 기본 금액(-1)을 반환한다.")
    public void getRoomCharge_fallback_success() throws Exception {
        // Given
        Hotel hotel = new Hotel(1L,"Marriott Hotel Seoul","South Korea", "Seoul", "address", "description");
        LocalDate date = LocalDate.parse("2025-01-04");

        int expectedFallbackCharge = -1;
        String errorMessage = "FeignClient Exception";
        Throwable throwable = new RuntimeException(errorMessage);

        // When
        int fallbackCharge = chargeFeignClientAdapter.fallbackGetRoomCharge(hotel, date, throwable);

        // Then
        assertEquals(expectedFallbackCharge, fallbackCharge);
    }
}
