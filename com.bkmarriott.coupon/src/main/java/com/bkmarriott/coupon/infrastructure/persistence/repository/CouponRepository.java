package com.bkmarriott.coupon.infrastructure.persistence.repository;

import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

}
