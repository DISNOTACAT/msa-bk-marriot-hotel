package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.UserCouponOutputPort;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.UserCouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserCouponCommandPersistenceAdapter implements UserCouponOutputPort {

    private final UserCouponRepository userCouponRepository;

    @Transactional
    public UserCoupon generateUserCoupon(UserCoupon userCoupon) {
        UserCouponEntity userCouponEntity = UserCouponEntity.from(userCoupon);
        userCouponEntity = userCouponRepository.save(userCouponEntity);

        return userCouponEntity.toDomain();
    }
}
