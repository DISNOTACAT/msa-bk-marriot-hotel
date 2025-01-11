package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.UserCouponOutputPort;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.UserCouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.exception.CouponNotSpentException;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import com.bkmarriott.coupon.infrastructure.persistence.exception.UserCouponNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCouponCommandPersistenceAdapter implements UserCouponOutputPort {

    private final UserCouponRepository userCouponRepository;

    @Override
    @Transactional
    public UserCoupon generateUserCoupon(UserCoupon userCoupon) {
        log.info("[UserCouponCommandPersistenceAdapter] [generateUserCoupon] couponId ::: {}", userCoupon.getId());

        UserCouponEntity userCouponEntity = UserCouponEntity.from(userCoupon);
        userCouponEntity = userCouponRepository.save(userCouponEntity);

        return userCouponEntity.toDomain();
    }

    @Override
    public UserCoupon findValidCouponById(Long userCouponId) {
        log.info("[UserCouponCommandPersistenceAdapter] [findValidCouponById] couponId ::: {}", userCouponId);

        UserCouponEntity userCouponEntity =
                userCouponRepository.findValidCouponById(userCouponId, LocalDateTime.now())
                        .orElseThrow(UserCouponNotFoundException::new);

        return userCouponEntity.toDomain();
    }

    @Override
    @Transactional
    public UserCoupon update(UserCoupon userCoupon) {
        log.info("[UserCouponCommandPersistenceAdapter] [update] couponId ::: {}", userCoupon.getId());

        UserCouponEntity userCouponEntity = userCouponRepository.findValidCouponById(userCoupon.getId(), LocalDateTime.now())
                .orElseThrow(UserCouponNotFoundException::new);

        userCouponEntity = userCouponEntity.updateSpentAt(userCoupon);
        userCouponEntity = userCouponRepository.save(userCouponEntity);

        return userCouponEntity.toDomain();
    }

    @Override
    @Transactional
    public UserCoupon cancelUserCouponUsage(UserCoupon userCoupon) {
        log.info("[UserCouponCommandPersistenceAdapter] [cancelUserCouponUsage] couponId ::: {}", userCoupon.getId());

        UserCouponEntity userCouponEntity = userCouponRepository.findById(userCoupon.getId())
                .orElseThrow(UserCouponNotFoundException::new);

        userCouponEntity = userCouponEntity.updateSpentAt(userCoupon);
        userCouponEntity = userCouponRepository.save(userCouponEntity);

        return userCouponEntity.toDomain();
    }

    @Override
    public UserCoupon findById(Long id) {
        log.info("[UserCouponCommandPersistenceAdapter] [findById] couponId ::: {}", id);

        return userCouponRepository.findById(id)
                .map(UserCouponEntity::toDomain)
                .orElseThrow(UserCouponNotFoundException::new);
    }
}
