package com.bkmarriott.hotel.application.service;

import com.bkmarriott.hotel.application.dto.HotelSearchResponseDto;
import com.bkmarriott.hotel.application.outputport.ChargeOutputPort;
import com.bkmarriott.hotel.application.outputport.HotelQueryOutputPort;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomType;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] HotelService Unit Test")
public class HotelQueryServiceTest {

    @InjectMocks private HotelQueryService hotelQueryService;
    @Mock private HotelQueryOutputPort hotelQueryOutputPort;
    @Mock private ChargeOutputPort chargeOutputPort;

    @Nested
    @DisplayName("호텔 검색 성공")
    class SuccessSearchHotel{
        @Test
        @DisplayName("요청받은 조건에 맞는 호텔 정보를 반환한다.")
        public void searchHotel_success() throws Exception {
            // Given
            HotelSearchRequest request = new HotelSearchRequest("Marriott", "Seoul", LocalDate.parse("2025-01-02"), LocalDate.parse("2025-01-03"));
            Pageable pageable = PageRequest.of(0, 10);

            Hotel hotel1 = new Hotel(1L,"Marriott Hotel Seoul1","South Korea", "Seoul", "address1", "description1");
            Hotel hotel2 = new Hotel(2L,"Marriott Hotel Seoul2","South Korea", "Seoul", "address2", "description2");
            Hotel hotel3 = new Hotel(3L,"Marriott Hotel Seoul3","South Korea", "Seoul", "address3", "description3");
            Page<Hotel> mockResponse = new PageImpl<>(List.of(hotel1, hotel2, hotel3));

            Mockito.when(hotelQueryOutputPort.searchHotel(request, pageable)).thenReturn(mockResponse);

            int expectedCharge = 100000;
            List<Long> hotelIds = mockResponse.getContent().stream().map(Hotel::getHotelId).toList();
            List<RoomChargeResponse> mockRoomCharge = new ArrayList<>();
            for(Long hotelId : hotelIds){
                mockRoomCharge.add(new RoomChargeResponse(hotelId, RoomType.STANDARD, expectedCharge, request.startDate()));
            }

            Mockito.when(chargeOutputPort.getRoomCharge(hotelIds, request.startDate())).thenReturn(mockRoomCharge);

            // When
            Page<HotelSearchResponseDto> result = hotelQueryService.searchHotel(request, pageable);

            // Then
            assertEquals(3, result.getContent().size());

            for(int i=0; i<result.getContent().size(); i++){
                HotelSearchResponseDto dto = result.getContent().get(i);
                Hotel hotel = mockResponse.getContent().get(i);
                assertEquals(hotel.getHotelId(), dto.getHotelId());
                assertEquals(expectedCharge, dto.getCharge());
            }
        }

        @Test
        @DisplayName("호텔의 요금 정보가 조회하지 못할 경우 호텔 요금에 null 값이 반환된다.")
        public void searchHotel_success_chargeNull() throws Exception {
            // Given
            HotelSearchRequest request = new HotelSearchRequest("Marriott", "Seoul", LocalDate.parse("2025-01-02"), LocalDate.parse("2025-01-03"));
            Pageable pageable = PageRequest.of(0, 10);

            Hotel hotel1 = new Hotel(1L,"Marriott Hotel Seoul1","South Korea", "Seoul", "address1", "description1");
            Hotel hotel2 = new Hotel(2L,"Marriott Hotel Seoul2","South Korea", "Seoul", "address2", "description2");
            Hotel hotel3 = new Hotel(3L,"Marriott Hotel Seoul3","South Korea", "Seoul", "address3", "description3");
            Page<Hotel> mockResponse = new PageImpl<>(List.of(hotel1, hotel2, hotel3));

            Mockito.when(hotelQueryOutputPort.searchHotel(request, pageable)).thenReturn(mockResponse);

            int expectedCharge = 100000;
            List<Long> hotelIds = mockResponse.getContent().stream().map(Hotel::getHotelId).toList();
            List<RoomChargeResponse> mockRoomCharge = List.of(
                    new RoomChargeResponse(1L, RoomType.STANDARD, expectedCharge, request.startDate()),
                    new RoomChargeResponse(2L, RoomType.STANDARD, expectedCharge, request.startDate())
            );

            Mockito.when(chargeOutputPort.getRoomCharge(hotelIds, request.startDate())).thenReturn(mockRoomCharge);

            // When
            Page<HotelSearchResponseDto> result = hotelQueryService.searchHotel(request, pageable);

            // Then
            assertEquals(3, result.getContent().size());

            for(int i = 0; i < result.getContent().size(); i++) {
                HotelSearchResponseDto dto = result.getContent().get(i);
                Hotel hotel = mockResponse.getContent().get(i);

                if (hotel.getHotelId() == 3L) {
                    assertNull(dto.getCharge());
                } else {
                    assertEquals(expectedCharge, dto.getCharge());
                }
            }
        }
        @Test
        @DisplayName("검색 조건에 맞는 호텔 정보가 없을 경우 빈 페이지 값을 반환한다.")
        public void searchHotel_success_noContent() throws Exception {
            // Given
            HotelSearchRequest request = new HotelSearchRequest("Nonexistent Hotel", "Unknown", LocalDate.parse("2025-01-02"), LocalDate.parse("2025-01-03"));
            Pageable pageable = PageRequest.of(0, 10);

            Page<Hotel> mockResponse = Page.empty(pageable);

            Mockito.when(hotelQueryOutputPort.searchHotel(request, pageable)).thenReturn(mockResponse);

            // When
            Page<HotelSearchResponseDto> result = hotelQueryService.searchHotel(request, pageable);

            // Then
            Assertions.assertAll(
                    () -> assertThat(result.getContent()).isEmpty(),  // 검색된 호텔 정보가 없으므로 빈 리스트여야 한다.
                    () -> assertEquals(0, result.getTotalElements())  // 결과의 전체 요소 수는 0이어야 한다.
            );
        }
    }
}
