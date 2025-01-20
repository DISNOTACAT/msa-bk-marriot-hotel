package com.bkmarriott.hotel.presentation.rest.controller;

import com.bkmarriott.hotel.application.service.HotelQueryService;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import com.bkmarriott.hotel.presentation.rest.dto.response.HotelSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotels")
public class HotelQueryController {

    private final HotelQueryService hotelQueryService;

    @GetMapping
    public ResponseEntity<Page<HotelSearchResponse>> searchHotel(@Valid HotelSearchRequest searchRequest, @PageableDefault Pageable pageable){

        return ResponseEntity.ok().body(hotelQueryService.searchHotel(searchRequest, pageable).map(HotelSearchResponse::toDto));
    }
}
