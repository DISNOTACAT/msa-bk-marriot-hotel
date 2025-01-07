package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.CouponPolicy;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntityType;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("[Infrastructure] [Integration] UserCoupon Adapter Test")
@RepositoryTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserCouponAdapterTest {

    @Autowired
    private UserCouponQueryAdapter memberCouponQueryAdapter;

    @Autowired
    private UserCouponCommandPersistenceAdapter userCouponCommandAdapter;

    @Test
    @Transactional
    @DisplayName("[성공] 유효한 쿠폰 조회 테스트 - 쿠폰 아이디로 기간이 유효하고 미사용인 쿠폰이 있으면 반환")
    void getValidMemberCoupon_successTest() {
        // Given
        UserCoupon userCoupon = generateTestMemberCoupon();

        // When
        UserCoupon generatedCoupon = userCouponCommandAdapter.generateUserCoupon(userCoupon);
        UserCoupon actual = memberCouponQueryAdapter.getById(generatedCoupon.getId());

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(actual),
                () -> Assertions.assertEquals(generatedCoupon.getId(), actual.getId()),
                () -> Assertions.assertTrue(actual.getIssuedAt().isBefore(LocalDateTime.now())),
                () -> Assertions.assertTrue(actual.getExpiredAt().isAfter(LocalDateTime.now())),
                () -> Assertions.assertNull(actual.getSpentAt())
        );
    }

    private UserCoupon generateTestMemberCoupon() {
        // Test (2024.01.04 11:55:00) 시점에서 유효한 테스트 쿠폰 발급
        return new UserCoupon(
                null,
                generateTestCoupon(),
                1234L,
                LocalDateTime.of(2025, 1, 4, 0, 0, 0),
                null,
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );
    }

    private Coupon generateTestCoupon() {
        return new Coupon(
                1L,
                generateTestCouponPolicyFixedType(),
                "Test Coupon",
                0.1f
        );
    }

    private CouponPolicy generateTestCouponPolicyFixedType() {
        return new CouponPolicy(
                1234L,
                CouponPolicyType.FIXED,
                null,
                LocalDateTime.of(2025, 1, 3, 0, 0, 0),
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );
    }
}