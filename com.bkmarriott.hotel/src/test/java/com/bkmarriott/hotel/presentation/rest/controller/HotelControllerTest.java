package com.bkmarriott.hotel.presentation.rest.controller;

import com.bkmarriott.hotel.application.dto.HotelSearchResponseDto;
import com.bkmarriott.hotel.application.service.HotelService;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(HotelController.class)
@DisplayName("[Presentation] HotelController Unit Test")
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    @DisplayName("[호텔 검색 성공 테스트] 호텔을 검색한다.")
    void searchHotel_success() throws Exception {
        // Given
        String name = "Marriott";
        String city = "Seoul";
        String startDate = "2025-01-02";
        String endDate = "2025-01-03";

        String requestUrl = "/api/v1/hotels";


        Page<HotelSearchResponseDto> mockResponse = new PageImpl<>(List.of(
                new HotelSearchResponseDto(new Hotel(1L,"Marriott Hotel Seoul","South Korea", "Seoul", "address", "description"), 10000)
        ));
        Mockito.when(hotelService.searchHotel(ArgumentMatchers.any(HotelSearchRequest.class), ArgumentMatchers.any(Pageable.class)))
                        .thenReturn(mockResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
                    .param("name", name)
                    .param("city", city)
                    .param("startDate", startDate)
                    .param("endDate", endDate)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Marriott Hotel Seoul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Seoul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].charge").value(10000))
                .andDo(print());
    }

    @Test
    @DisplayName("[호텔 검색 실패 테스트] 검색 조건이 올바르지 않으면 유효성 검사에 실패한다.")
    void searchHotel_validation_failure() throws Exception {
        // Given
        String name = " ";
        String city = "Seoul";
        String startDate = null;
        String endDate = null;

        String requestUrl = "/api/v1/hotels";


        Page<HotelSearchResponseDto> mockResponse = new PageImpl<>(List.of(
                new HotelSearchResponseDto(new Hotel(1L,"Marriott Hotel Seoul","South Korea", "Seoul", "address", "description"), 10000)
        ));
        Mockito.when(hotelService.searchHotel(ArgumentMatchers.any(HotelSearchRequest.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(mockResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl)
                        .param("name", name)
                        .param("city", city)
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

}
