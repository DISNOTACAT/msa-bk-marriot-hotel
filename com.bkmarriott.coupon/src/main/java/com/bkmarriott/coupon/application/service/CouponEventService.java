package com.bkmarriott.coupon.application.service;

import com.bkmarriott.coupon.application.exception.EventDuplicateException;
import com.bkmarriott.coupon.application.outputport.CouponEventLogOutputPort;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.event.CouponIssuanceEvent;
import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
import com.bkmarriott.coupon.domain.vo.UserCouponForIssue;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponEventService {

    private final CouponEventLogOutputPort couponEventLogOutputPort;
    private final UserCouponService userCouponService;

    public UserCoupon issueCoupon(DomainEventEnvelop<CouponIssuanceEvent> envelop) {
        checkDuplicationEvent(envelop.getEventId());
        UserCouponForIssue userCouponForIssue = envelop.getEvent().toUserCouponForIssue();

        UserCoupon userCoupon = userCouponService.issueCoupon(userCouponForIssue);
        couponEventLogOutputPort.saveLog(envelop);

        return userCoupon;
    }

    private void checkDuplicationEvent(UUID eventId) {
        String eventLogId = String.valueOf(eventId);
        boolean isDuplicated = couponEventLogOutputPort.isExistedCouponLog(eventLogId);
        if (isDuplicated) {
            throw new EventDuplicateException();
        }
    }
}
