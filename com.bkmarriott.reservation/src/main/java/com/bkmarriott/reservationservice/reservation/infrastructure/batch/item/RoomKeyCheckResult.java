package com.bkmarriott.reservationservice.reservation.infrastructure.batch.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomKeyCheckResult {
    private String redisRoomKey;
    private long roomStock;
}
