package com.bkmarriott.hotel.application.outputport;

import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelQueryOutputPort {

    Page<Hotel> searchHotel(HotelSearchRequest searchRequest, Pageable pageable);

}
