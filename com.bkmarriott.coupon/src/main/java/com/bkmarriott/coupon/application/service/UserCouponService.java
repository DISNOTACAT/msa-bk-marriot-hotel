package com.bkmarriott.coupon.application.service;

import com.bkmarriott.coupon.application.outputport.UserCouponOutputPort;
import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.vo.UserCouponForIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCouponService {

    private final CouponService couponService;
    private final UserCouponOutputPort userCouponOutputPort;

    public UserCoupon issueCoupon(UserCouponForIssue userCouponForIssue) {
        Coupon coupon = couponService.findCoupon(userCouponForIssue.couponId());
        UserCoupon userCoupon = userCouponForIssue.toUserCoupon(coupon);

        return userCouponOutputPort.generateUserCoupon(userCoupon);
    }

    public UserCoupon useUserCoupon(Long id) {
        UserCoupon userCoupon = userCouponOutputPort.findValidCouponById(id);
        userCoupon = userCoupon.updateSpentAt();
        return userCouponOutputPort.update(userCoupon)
    }
}
