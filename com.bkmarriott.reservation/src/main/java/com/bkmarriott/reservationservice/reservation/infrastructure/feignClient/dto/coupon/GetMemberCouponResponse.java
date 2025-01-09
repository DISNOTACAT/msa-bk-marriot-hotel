package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.coupon;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetMemberCouponResponse {

    private Long id;
    private Coupon coupon;
    private Long memberId;
    private LocalDateTime issuanceAt;
    private LocalDateTime spendingAt;
    private LocalDateTime expiredAt;

}
