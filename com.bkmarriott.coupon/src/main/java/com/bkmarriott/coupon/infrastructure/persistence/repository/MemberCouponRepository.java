package com.bkmarriott.coupon.infrastructure.persistence.repository;

import com.bkmarriott.coupon.infrastructure.persistence.entity.MemberCouponEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCouponRepository extends JpaRepository<MemberCouponEntity, Long> {

    @Query("""
            SELECT mc
            FROM MemberCoupon mc
            JOIN FETCH mc.coupon c
            JOIN FETCH c.couponPolicy cp
            WHERE mc.id = :id
            AND :requestDate BETWEEN mc.issuanceAt AND mc.expiredAt
            AND mc.spendingAt IS NULL
            """)
    Optional<MemberCouponEntity> findValidCouponById(@Param("id") Long id, @Param("requestDate") LocalDateTime requestDate);

}
