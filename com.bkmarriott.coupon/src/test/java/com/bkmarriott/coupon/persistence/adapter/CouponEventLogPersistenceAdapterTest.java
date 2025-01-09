package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.event.DomainEventEnvelop;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.CouponEventLogPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponIssuanceEventLogEntity;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponIssuanceEventLogRepository;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] [Integration] CouponEventLogPersistenceAdapter Test")
@RepositoryTest
class CouponEventLogPersistenceAdapterTest {

    private static CouponIssuanceEventLogEntity testLogEntity;

    @Autowired
    private CouponEventLogPersistenceAdapter couponEventLogAdapter;

    @Autowired
    private CouponIssuanceEventLogRepository eventLogRepository;

    @BeforeEach
    void setUp() {
        eventLogRepository.deleteAll();

        CouponIssuanceEventLogEntity entity = new CouponIssuanceEventLogEntity("testUuid");
        testLogEntity = eventLogRepository.save(entity);
    }

    @Test
    @DisplayName("[성공] 데이터 존재 확인 테스트 - 아이디로 검색 결과가 존재할 시 true 반환")
    void isExistedCouponLog_successTest_existed() {
        // Given
        String logId = testLogEntity.getId();
        // When
        boolean actual = couponEventLogAdapter.isExistedCouponLog(logId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(actual)
        );
    }

    @Test
    @DisplayName("[성공] 데이터 존재 확인 테스트 - 아이디로 검색 결과가 존재하지 않을 시 false 반환")
    void isExistedCouponLog_successTest_notExisted() {
        // Given
        String logId = "InvalidLogId";
        // When
        boolean actual = couponEventLogAdapter.isExistedCouponLog(logId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(actual)
        );
    }

    @Test
    @DisplayName("[성공] 로그 저장 테스트 - 로그 저장 후 식별자를 반환")
    void saveLog_successTest() {
        // Given
        UUID eventId = UUID.randomUUID();
        DomainEventEnvelop mockEnvelop = Mockito.mock(DomainEventEnvelop.class);
        Mockito.when(mockEnvelop.getEventId()).thenReturn(eventId);
        // When
        String actual = couponEventLogAdapter.saveLog(mockEnvelop);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(String.valueOf(eventId), actual)
        );
    }
}