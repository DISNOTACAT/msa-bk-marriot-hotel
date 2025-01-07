package com.bkmarriott.promotion.infrastructure.persistence.config;

import com.bkmarriott.promotion.infrastructure.persistence.adapter.CouponEventPersistenceAdapter;
import com.bkmarriott.promotion.infrastructure.persistence.adapter.PromotionPersistenceAdapter;
import com.bkmarriott.promotion.infrastructure.persistence.repository.CouponIssuanceOutboxRepository;
import com.bkmarriott.promotion.infrastructure.persistence.repository.PromotionRepository;
import com.bkmarriott.promotion.infrastructure.persistence.util.EventConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceTestConfig {

    private final CouponIssuanceOutboxRepository couponIssuanceOutboxRepository;
    private final PromotionRepository promotionRepository;

    public PersistenceTestConfig(
        CouponIssuanceOutboxRepository couponIssuanceOutboxRepository,
        PromotionRepository promotionRepository
    ) {
        this.couponIssuanceOutboxRepository = couponIssuanceOutboxRepository;
        this.promotionRepository = promotionRepository;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public EventConverter eventConverter() {
        return new EventConverter(objectMapper());
    }

    @Bean
    public CouponEventPersistenceAdapter couponEventPersistenceAdapter() {
        return new CouponEventPersistenceAdapter(couponIssuanceOutboxRepository, eventConverter());
    }

    @Bean
    public PromotionPersistenceAdapter promotionPersistenceAdapter() {
        return new PromotionPersistenceAdapter(promotionRepository);
    }
}
