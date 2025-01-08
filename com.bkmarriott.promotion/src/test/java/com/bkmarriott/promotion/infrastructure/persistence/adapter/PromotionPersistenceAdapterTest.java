package com.bkmarriott.promotion.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.infrastructure.persistence.config.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] [Integration] PromotionPersistenceAdapter Test")
@RepositoryTest
class PromotionPersistenceAdapterTest {

    @Autowired
    private PromotionPersistenceAdapter promotionPersistenceAdapter;

    @Test
    @DisplayName("성공 - 프로모션 조회 테스트 - 유요한 식별자가 주어진 경우 도메인 객체를 Optional에 담아 반환")
    void findPromotionById_successTest_matchedId() {
        // Given
        Long promotionId = 1L;
        // When
        Optional<Promotion> promotionOptional =
            promotionPersistenceAdapter.findPromotionById(promotionId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(promotionOptional.isPresent())
        );
    }

    @Test
    @DisplayName("성공 - 프로모션 조회 테스트 - 유효하지 않은 식별자가 주어진 경우 빈 Optional 반환")
    void findPromotionById_successTest_notMatchedId() {
        // Given
        Long promotionId = 100L;
        // When
        Optional<Promotion> promotionOptional =
            promotionPersistenceAdapter.findPromotionById(promotionId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(promotionOptional.isPresent())
        );
    }
}