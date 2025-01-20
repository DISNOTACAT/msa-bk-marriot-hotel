package com.bkmarriott.promotion.application.service;

import com.bkmarriott.promotion.application.outputport.PromotionReader;
import com.bkmarriott.promotion.domain.Promotion;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class
PromotionService {
    
    private final PromotionReader promotionReader;
    
    public Promotion findPromotionInProgress(Long promotionId, LocalDateTime targetDateTime) {
        Promotion promotion = promotionReader.findPromotionById(promotionId).orElseThrow(() ->
            new IllegalArgumentException("프로모션 정보가 존재하지 않습니다."));

        if (!promotion.isInProgressWhen(targetDateTime)) {
            throw new IllegalArgumentException("프로모션 진행 기간이 아닙니다.");
        }
        return promotion;
    }
}
