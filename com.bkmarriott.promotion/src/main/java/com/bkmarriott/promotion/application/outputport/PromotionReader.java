package com.bkmarriott.promotion.application.outputport;

import com.bkmarriott.promotion.domain.Promotion;
import java.util.Optional;

public interface PromotionReader {

    Optional<Promotion> findPromotionById(Long promotionId);
}
