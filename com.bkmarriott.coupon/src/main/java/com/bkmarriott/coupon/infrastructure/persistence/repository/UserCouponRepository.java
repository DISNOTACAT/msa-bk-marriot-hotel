package com.bkmarriott.coupon.infrastructure.persistence.repository;

import com.bkmarriott.coupon.infrastructure.persistence.entity.UserCouponEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long> {

    @Query("""
            SELECT uc
            FROM UserCoupon uc
            JOIN FETCH uc.coupon c
            JOIN FETCH c.couponPolicy cp
            WHERE uc.id = :id
            AND :requestDate BETWEEN uc.issuanceAt AND uc.expiredAt
            AND uc.spendingAt IS NULL
            """)
    Optional<UserCouponEntity> findValidCouponById(@Param("id") Long id, @Param("requestDate") LocalDateTime requestDate);

    Page<UserCouponEntity> findUserCouponListByUserId(Long userId, Pageable pageable);
}
