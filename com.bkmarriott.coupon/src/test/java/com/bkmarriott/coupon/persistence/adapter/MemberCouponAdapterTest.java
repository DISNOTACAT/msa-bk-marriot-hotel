package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.CouponPolicy;
import com.bkmarriott.coupon.domain.MemberCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.MemberCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntityType;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("[Infrastructure] MemberCoupon Repository Unit Test")
@RepositoryTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberCouponAdapterTest {

    @Autowired
    private MemberCouponQueryAdapter memberCouponQueryAdapter;

    @Test
    @Transactional
    @DisplayName("[유효한 쿠폰 조회] 쿠폰 아이디로 기간이 유효하고 미사용인 쿠폰이 있으면 반환한다.")
    void getValidMemberCoupon_successTest() {
        // Given
        MemberCoupon memberCoupon = generateTestMemberCoupon();

        // When
        MemberCoupon generatedCoupon = memberCouponQueryAdapter.generateMemberCoupon(memberCoupon);
        MemberCoupon actual = memberCouponQueryAdapter.getById(generatedCoupon.getId());

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(actual),
                () -> Assertions.assertEquals(generatedCoupon.getId(), actual.getId()),
                () -> Assertions.assertTrue(actual.getIssuanceAt().isBefore(LocalDateTime.now())),
                () -> Assertions.assertTrue(actual.getExpiredAt().isAfter(LocalDateTime.now())),
                () -> Assertions.assertNull(actual.getSpendingAt())
        );
    }

    private MemberCoupon generateTestMemberCoupon() {
        // Test (2024.01.04 11:55:00) 시점에서 유효한 테스트 쿠폰 발급
        return new MemberCoupon(
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
                CouponPolicyEntityType.FIXED,
                null,
                LocalDateTime.of(2025, 1, 3, 0, 0, 0),
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );
    }
}