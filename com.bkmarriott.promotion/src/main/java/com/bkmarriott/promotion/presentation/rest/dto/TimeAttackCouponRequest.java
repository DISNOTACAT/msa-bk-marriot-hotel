package com.bkmarriott.promotion.presentation.rest.dto;

import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeAttackCouponRequest {

    private Long promotionId;

    public TimeAttackCouponIssuance toDomain(Long userId) {
        return new TimeAttackCouponIssuance(userId, promotionId, LocalDateTime.now());
    }
}
