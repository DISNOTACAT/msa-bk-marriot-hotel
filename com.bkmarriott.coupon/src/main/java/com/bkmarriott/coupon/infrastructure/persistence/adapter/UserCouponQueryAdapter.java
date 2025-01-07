package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.UserCouponOutputPort;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.UserCouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import com.bkmarriott.coupon.presentation.rest.exception.UserCouponNotFoundException;
import com.bkmarriott.coupon.presentation.rest.ouputport.UserCouponQueryOutport;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCouponQueryAdapter {

    private final UserCouponRepository userCouponRepository;

    public UserCoupon getById(Long id) {
        return userCouponRepository.findValidCouponById(id, LocalDateTime.now())
                .map(UserCouponEntity::toDomain)
                .orElseThrow(UserCouponNotFoundException::new);
    }
}
