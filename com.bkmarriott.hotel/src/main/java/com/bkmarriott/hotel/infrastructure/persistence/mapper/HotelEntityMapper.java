package com.bkmarriott.hotel.infrastructure.persistence.mapper;


import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.persistence.entity.HotelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HotelEntityMapper {

    HotelEntityMapper HOTEL_ENTITY_MAPPER = Mappers.getMapper(HotelEntityMapper.class);

    Hotel mapToHotel(HotelEntity entity);

    List<Hotel> mapToHotelList(List<HotelEntity> entities);

}
