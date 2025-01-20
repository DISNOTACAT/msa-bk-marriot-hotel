package com.bkmarriott.promotion.infrastructure.persistence.adapter;

import com.bkmarriott.promotion.domain.event.CouponIssuanceEvent;
import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import com.bkmarriott.promotion.infrastructure.persistence.config.RepositoryTest;
import com.bkmarriott.promotion.infrastructure.persistence.entity.CouponIssuanceOutboxEntity;
import com.bkmarriott.promotion.infrastructure.persistence.repository.CouponIssuanceOutboxRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] [Integration] CouponEventPersistenceAdapter Test")
@RepositoryTest
class CouponEventPersistenceAdapterTest {

    @Autowired
    private CouponEventPersistenceAdapter couponEventPersistenceAdapter;

    @Autowired
    private CouponIssuanceOutboxRepository outboxRepository;

    @BeforeEach
    void setUp() {
        outboxRepository.deleteAll();
    }

    @Test
    @DisplayName("[성공] 발급 이전 데이터 조회 테스트 - isPublished 가 false인 엔티티 리스트를 반환한다.")
    void findBeforePublished_successTest() {
        // Given
        CouponIssuanceEvent couponIssuanceEvent =
            new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        DomainEventEnvelop<CouponIssuanceEvent> envelop = DomainEventEnvelop.of(couponIssuanceEvent, "testSource");

        couponEventPersistenceAdapter.record(envelop);
        // When
        List<CouponIssuanceOutboxEntity> actual = couponEventPersistenceAdapter.findBeforePublished();
        // Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(actual.isEmpty())
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 이벤트 저장 테스트 - DomainEventEnvelop 객체가 주어진 경우 Envelop 생성 후 영속화")
    void record_successTest_givenDomainEventEnvelop() {
        // Given
        CouponIssuanceEvent couponIssuanceEvent =
            new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        DomainEventEnvelop<CouponIssuanceEvent> envelop = DomainEventEnvelop.of(couponIssuanceEvent, "testSource");
        // When
        DomainEventEnvelop<CouponIssuanceEvent> actual = couponEventPersistenceAdapter.record(envelop);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(envelop.getEventId(), actual.getEventId()),
            () -> Assertions.assertFalse(outboxRepository.findAll().isEmpty())
        );
    }

    @Test
    @DisplayName("[성공] outbox 상태 변경 테스트 - id에 해당하는 엔티티의 isPublished 데이터를 true로 변경")
    public void recordToPublished_successTest() {
        // Given
        CouponIssuanceEvent couponIssuanceEvent =
            new CouponIssuanceEvent(1L, 1L, 1L, LocalDateTime.now());
        DomainEventEnvelop<CouponIssuanceEvent> envelop = DomainEventEnvelop.of(couponIssuanceEvent, "testSource");
        envelop = couponEventPersistenceAdapter.record(envelop);
        // When
        couponEventPersistenceAdapter.recordToPublished(envelop.getEventId());
        List<CouponIssuanceOutboxEntity> beforePublished = couponEventPersistenceAdapter.findBeforePublished();
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(beforePublished.isEmpty())
        );
    }
}