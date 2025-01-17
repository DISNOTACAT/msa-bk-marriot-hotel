package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.ChargeClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.RoomChargeResponse;
import java.time.LocalDate;
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
@DisplayName("[Infrastructure] CouponFeignClientAdapterTest Unit Test")
class ChargeFeignClientAdapterTest {

    @InjectMocks
    ChargeFeignClientAdapter chargeFeignClientAdapter;
    @Mock
    ChargeClient chargeClient;

    @Test
    @DisplayName("[성공] 객실 요금 FeignClient 테스트 - 조회 기간의 요금 정보를 반환한다.")
    void verifyCoupon_successTest(){
        // Given
        Long hotelId = 1L;
        RoomType roomType = RoomType.DELUXE;
        LocalDate startDate = LocalDate.of(2025,2,1);
        LocalDate endDate = LocalDate.of(2025,2,2);
        int charge = 100000;
        InventoryQuery query = new InventoryQuery(hotelId,startDate,endDate,roomType);
        RoomChargeResponse response = new RoomChargeResponse(hotelId,roomType,charge,startDate);

        Mockito.when(chargeClient.findRoomChargeByDates(
            ArgumentMatchers.anyLong(), ArgumentMatchers.any(RoomType.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)
            )).thenReturn(List.of(response));

        // When
        int actual = chargeFeignClientAdapter.findRoomChargeByDates(query);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(response.charge(),actual)
        );
    }
}