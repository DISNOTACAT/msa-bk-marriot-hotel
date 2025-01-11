package com.bkmarriott.reservationservice.reservation.application.outputport.cache;

import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;

public interface InventoryCacheOutputPort {

    void decreaseRoomCount(InventoryQuery query);

    void rollbackCount(InventoryQuery query);
}
