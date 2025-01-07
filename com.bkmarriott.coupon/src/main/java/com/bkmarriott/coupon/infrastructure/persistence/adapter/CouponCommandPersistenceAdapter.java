package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.CouponOutputPort;
import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponCommandPersistenceAdapter implements CouponOutputPort {

    private final CouponRepository couponRepository;

    public Optional<Coupon> findById(Long couponId) {
        log.info("[CouponPersistenceAdapter] [findById] couponId ::: {}", couponId);
        return couponRepository.findById(couponId)
            .map(CouponEntity::toDomain)
            .or(Optional::empty);
    }
}
