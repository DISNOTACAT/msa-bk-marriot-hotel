package com.bkmarriott.promotion.domain.vo;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] [Unit] TimeAttackCouponIssuance VO Test")
class TimeAttackCouponIssuanceTest {

    @Test
    @DisplayName("[성공] 객체 생성 테스트 - 올바른 정보가 주어진 경우 객체를 생성한다.")
    void TimeAttackCouponIssuanceConstruct_successTest() {
        // Given
        Long requestUserId = 1L;
        Long targetPromotionId = 2L;
        LocalDateTime requestDateTime = LocalDateTime.now();
        // When
        TimeAttackCouponIssuance actual =
            new TimeAttackCouponIssuance(requestUserId, targetPromotionId, requestDateTime);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(requestUserId, actual.getRequestUserId()),
            () -> Assertions.assertEquals(targetPromotionId, actual.getTargetPromotionId()),
        () -> Assertions.assertEquals(requestDateTime, actual.getRequestTime())
        );
    }
}