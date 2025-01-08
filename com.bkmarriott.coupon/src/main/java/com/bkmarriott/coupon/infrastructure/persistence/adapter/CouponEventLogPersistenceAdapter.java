package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.CouponEventLogOutputPort;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponIssuanceEventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponEventLogPersistenceAdapter implements CouponEventLogOutputPort {

    private final CouponIssuanceEventLogRepository couponIssuanceEventLogRepository;

    @Override
    public boolean isExistedCouponLog(String logId) {
        return couponIssuanceEventLogRepository.findById(logId).isPresent();
    }
}
