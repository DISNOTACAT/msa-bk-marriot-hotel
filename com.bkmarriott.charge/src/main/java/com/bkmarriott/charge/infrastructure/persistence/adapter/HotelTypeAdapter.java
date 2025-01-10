package com.bkmarriott.charge.infrastructure.persistence.adapter;

import com.bkmarriott.charge.application.outputport.HotelTypeOutputPort;
import com.bkmarriott.charge.domain.HotelType;
import com.bkmarriott.charge.infrastructure.persistence.entity.HotelTypeEntity;
import com.bkmarriott.charge.infrastructure.persistence.repository.HotelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HotelTypeAdapter implements HotelTypeOutputPort {

    private final HotelTypeRepository hotelTypeRepository;

    public List<HotelType> findAll() {
        return hotelTypeRepository.findAllByOrderByHotelIdAsc()
                .stream()
                .map(HotelTypeEntity::toDomain)
                .toList();
    }
}
