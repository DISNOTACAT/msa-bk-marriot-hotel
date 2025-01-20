package com.bkmarriott.promotion.infrastructure.persistence.adapter;

import com.bkmarriott.promotion.application.outputport.PromotionReader;
import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.infrastructure.persistence.entity.PromotionEntity;
import com.bkmarriott.promotion.infrastructure.persistence.repository.PromotionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PromotionPersistenceAdapter implements PromotionReader {

    private final PromotionRepository promotionRepository;

    @Override
    public Optional<Promotion> findPromotionById(Long promotionId) {
        return promotionRepository.findById(promotionId)
            .map(PromotionEntity::toDomain)
            .or(Optional::empty);
    }
}
