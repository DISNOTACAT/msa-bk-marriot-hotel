package com.bkmarriott.promotion.infrastructure.persistence.repository;

import com.bkmarriott.promotion.infrastructure.persistence.entity.CouponIssuanceOutboxEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CouponIssuanceOutboxRepository extends JpaRepository<CouponIssuanceOutboxEntity, Long> {

    @Modifying
    List<CouponIssuanceOutboxEntity> findAllByIsPublishedIsFalse();
}
