package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.coupon;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponPolicy {

    private Long id;
    private CouponPolicyType type;
    private Integer afterDay;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public LocalDateTime calcExpireTime(LocalDateTime baseTime) {
        return switch (type) {
            case FIXED -> endedAt;
            case AFTER -> baseTime.plusDays(afterDay);
            case MIXED -> {
                LocalDateTime endedAtForAfterDay = baseTime.plusDays(afterDay);
                yield (endedAt.isBefore(endedAtForAfterDay)) ? endedAt : endedAtForAfterDay;
            }
        };
    }
}
