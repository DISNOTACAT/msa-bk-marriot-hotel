package com.bkmarriott.reservationservice.reservation.domain.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomInventoryEvent {
    EventType changeType;       // 객실 재고 변경 상황 (예: 객실 선점, 주문 취소 등)
    List<RoomStockInfo> inventoryInfoList;


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class RoomStockInfo{
        Long sequenceNumber;     // Redis 요청 순서
        String redisRoomKey;     // Redis 키
        Long roomStock;          // 현재 객실 재고
    }

    private RoomInventoryEvent(EventType changeType, List<RoomStockInfo> inventoryInfoList) {
        this.changeType = changeType;
        this.inventoryInfoList = inventoryInfoList;
    }

    public static RoomInventoryEvent prepare(List<RoomStockInfo> inventoryInfoList){
        return new RoomInventoryEvent(EventType.PREPARED, inventoryInfoList);
    }

    public static RoomInventoryEvent rollback(List<RoomStockInfo> inventoryInfoList){
        return new RoomInventoryEvent(EventType.ROLLBACK, inventoryInfoList);
    }
}
