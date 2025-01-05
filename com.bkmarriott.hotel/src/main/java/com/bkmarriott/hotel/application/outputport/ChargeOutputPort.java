package com.bkmarriott.hotel.application.outputport;

import com.bkmarriott.hotel.domain.Hotel;

import java.time.LocalDate;

public interface ChargeOutputPort {
    int getRoomCharge(Hotel hotel, LocalDate date);
}
