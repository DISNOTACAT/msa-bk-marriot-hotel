package com.bkmarriott.promotion.infrastructure.persistence.repository;

import com.bkmarriott.promotion.infrastructure.persistence.entity.CouponIssuanceOutboxEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssuanceOutboxRepository extends JpaRepository<CouponIssuanceOutboxEntity, String> {

    List<CouponIssuanceOutboxEntity> findAllByIsPublishedIsFalse();
}
