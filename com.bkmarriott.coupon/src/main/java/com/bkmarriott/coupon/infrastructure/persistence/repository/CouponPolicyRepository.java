package com.bkmarriott.coupon.infrastructure.persistence.repository;

import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicyEntity, Long> {

}
