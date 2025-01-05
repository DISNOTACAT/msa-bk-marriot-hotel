package com.bkmarriott.hotel.infrastructure.persistence.adapter;

import com.bkmarriott.hotel.application.outputport.HotelQueryOutputPort;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.persistence.repository.HotelQueryRepository;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelQueryAdapter implements HotelQueryOutputPort {

    private final HotelQueryRepository hotelQueryRepository;

    public Page<Hotel> searchHotel(HotelSearchRequest searchRequest, Pageable pageable){

        return hotelQueryRepository.searchHotel(searchRequest, pageable);
    }
}
