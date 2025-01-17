package com.bkmarriott.reservationservice.reservation.application.outputport.feign;

import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;


public interface ChargeOutputPort {
    int getRoomCharge(InventoryQuery query);
}
