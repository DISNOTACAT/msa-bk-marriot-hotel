package com.bkmarriott.promotion.infrastructure.persistence.repository;

import com.bkmarriott.promotion.infrastructure.persistence.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

}
