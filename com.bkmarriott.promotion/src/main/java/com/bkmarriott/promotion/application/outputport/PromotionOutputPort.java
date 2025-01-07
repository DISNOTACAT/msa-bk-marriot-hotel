package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.Promotion;
import java.util.Optional;

public interface PromotionOutputPort {

    Optional<Promotion> findPromotionById(Long promotionId);
}
