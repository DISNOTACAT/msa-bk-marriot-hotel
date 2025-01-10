package com.bkmarriott.charge.application.outputport;

import com.bkmarriott.charge.domain.HotelType;

import java.util.List;

public interface HotelTypeOutputPort {

    List<HotelType> findAll();
}
