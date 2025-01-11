package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.UserCouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import com.bkmarriott.coupon.infrastructure.persistence.exception.UserCouponNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCouponQueryAdapter {

    private final UserCouponRepository userCouponRepository;

    public UserCoupon getById(Long userCouponId) {
        log.info("[UserCouponQueryAdapter] [getById] couponId ::: {}", userCouponId);

        return userCouponRepository.findValidCouponById(userCouponId, LocalDateTime.now())
                .map(UserCouponEntity::toDomain)
                .orElseThrow(UserCouponNotFoundException::new);
    }
}
