package com.bkmarriott.hotel.application.service;

import com.bkmarriott.hotel.application.dto.HotelSearchResponseDto;
import com.bkmarriott.hotel.application.outputport.ChargeOutputPort;
import com.bkmarriott.hotel.application.outputport.HotelQueryOutputPort;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelQueryOutputPort hotelQueryOutputPort;
    private final ChargeOutputPort chargeOutputPort;

    public Page<HotelSearchResponseDto> searchHotel(HotelSearchRequest searchRequest, Pageable pageable) {

        Page<Hotel> hotels = hotelQueryOutputPort.searchHotel(searchRequest, pageable);

        return hotels.map(hotel -> {
            int roomCharge = chargeOutputPort.getRoomCharge(hotel, searchRequest.startDate());
            return new HotelSearchResponseDto(hotel, roomCharge);
        });
    }
}
