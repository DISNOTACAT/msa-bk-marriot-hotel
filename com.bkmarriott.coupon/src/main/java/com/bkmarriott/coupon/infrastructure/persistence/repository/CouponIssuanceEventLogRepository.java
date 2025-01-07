package com.bkmarriott.coupon.infrastructure.persistence.repository;

import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponIssuanceEventLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssuanceEventLogRepository extends JpaRepository<CouponIssuanceEventLogEntity, String> {

}
