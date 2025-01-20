package com.bkmarriott.hotel.application.service;

import com.bkmarriott.hotel.application.dto.HotelSearchResponseDto;
import com.bkmarriott.hotel.application.outputport.ChargeOutputPort;
import com.bkmarriott.hotel.application.outputport.HotelQueryOutputPort;
import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.feignClient.dto.RoomChargeResponse;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelQueryService {

    private final HotelQueryOutputPort hotelQueryOutputPort;
    private final ChargeOutputPort chargeOutputPort;

    public Page<HotelSearchResponseDto> searchHotel(HotelSearchRequest searchRequest, Pageable pageable) {

        Page<Hotel> hotels = hotelQueryOutputPort.searchHotel(searchRequest, pageable);

        if(!hotels.hasContent()){
            return Page.empty(pageable);
        }

        List<Long> hotelIds = hotels.getContent().stream().map(Hotel::getHotelId).toList();

        List<RoomChargeResponse> roomCharges = chargeOutputPort.getRoomCharge(hotelIds, searchRequest.startDate());

        Map<Long, RoomChargeResponse> roomChargeResponseMap = mapRoomChargesByHotelId(roomCharges);

        return hotels.map(hotel -> {
           RoomChargeResponse roomChargeResponse = roomChargeResponseMap.get(hotel.getHotelId());
           Integer roomCharge = (roomChargeResponse != null) ? roomChargeResponse.charge() : null;
           return new HotelSearchResponseDto(hotel, roomCharge);
        });
    }

    private Map<Long, RoomChargeResponse> mapRoomChargesByHotelId(List<RoomChargeResponse> roomCharges) {
        return roomCharges.stream()
                .collect(Collectors.toMap(RoomChargeResponse::hotelId, roomCharge -> roomCharge));
    }
}
