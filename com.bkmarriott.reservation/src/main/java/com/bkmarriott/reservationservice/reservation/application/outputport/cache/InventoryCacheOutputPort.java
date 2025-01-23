package com.bkmarriott.reservationservice.reservation.application.outputport.cache;

import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;

import java.util.List;

public interface InventoryCacheOutputPort {

    List<RoomInventoryEvent.RoomStockInfo> decreaseRoomCount(InventoryQuery query);

    List<RoomInventoryEvent.RoomStockInfo> rollbackCount(InventoryQuery query);
}
