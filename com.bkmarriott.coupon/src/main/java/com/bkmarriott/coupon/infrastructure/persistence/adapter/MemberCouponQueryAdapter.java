package com.bkmarriott.coupon.infrastructure.persistence.adapter;

import com.bkmarriott.coupon.application.outputport.MemberCouponOutputPort;
import com.bkmarriott.coupon.domain.MemberCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.entity.MemberCouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.MemberCouponRepository;
import com.bkmarriott.coupon.presentation.rest.exception.MemberCouponNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberCouponQueryAdapter implements MemberCouponOutputPort {
    private final MemberCouponRepository memberCouponRepository;

    public MemberCoupon getById(Long id) {
        return memberCouponRepository.findValidCouponById(id, LocalDateTime.now())
                .map(MemberCouponEntity::toDomain)
                .orElseThrow(MemberCouponNotFoundException::new);
    }

    @Transactional
    public MemberCoupon generateMemberCoupon(MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = memberCouponRepository.save(MemberCouponEntity.from(memberCoupon));
        return memberCouponEntity.toDomain();
    }
}
