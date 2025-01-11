package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.CouponPolicy;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntity;
import com.bkmarriott.coupon.infrastructure.persistence.exception.CouponNotSpentException;
import com.bkmarriott.coupon.infrastructure.persistence.exception.UserCouponNotFoundException;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponPolicyRepository;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponRepository;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DisplayName("[Infrastructure] [Integration] UserCoupon Adapter Test")
@RepositoryTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserCouponAdapterTest {

    @Autowired
    private UserCouponQueryAdapter userCouponQueryAdapter;

    @Autowired
    private UserCouponCommandPersistenceAdapter userCouponCommandAdapter;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("[성공] 유효한 쿠폰 조회 테스트 - 쿠폰 아이디로 기간이 유효하고 미사용인 쿠폰이 있으면 반환")
    void getValidUserCoupon_successTest() {
        // Given
        UserCoupon userCoupon = generateTestUserCoupon(null);

        // When
        UserCoupon generatedCoupon = userCouponCommandAdapter.generateUserCoupon(userCoupon);
        UserCoupon actual = userCouponQueryAdapter.getById(generatedCoupon.getId());

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(actual),
                () -> Assertions.assertEquals(generatedCoupon.getId(), actual.getId()),
                () -> Assertions.assertTrue(actual.getIssuedAt().isBefore(LocalDateTime.now())),
                () -> Assertions.assertTrue(actual.getExpiredAt().isAfter(LocalDateTime.now())),
                () -> Assertions.assertNull(actual.getSpentAt())
        );
    }

    private UserCoupon generateTestUserCoupon(Long userCouponId) {
        // Test (2024.01.04 11:55:00) 시점에서 유효한 테스트 쿠폰 발급
        return new UserCoupon(
                userCouponId,
                generateTestCoupon(),
                1234L,
                LocalDateTime.of(2025, 1, 4, 0, 0, 0),
                null,
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );
    }

    private UserCoupon generateTestUsedUserCoupon() {
        // Test (2024.01.04 11:55:00) 시점에서 유효한 테스트 쿠폰 발급
        return new UserCoupon(
                null,
                generateTestCoupon(),
                1234L,
                LocalDateTime.of(2025, 1, 4, 0, 0, 0),
                LocalDateTime.of(2025, 1, 8, 0, 0, 0),
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );
    }

    private Coupon generateTestCoupon() {
        Coupon coupon = new Coupon(
                null,
                generateTestCouponPolicyFixedType(),
                "Test Coupon",
                0.1f
        );

        CouponEntity couponEntity = couponRepository.save(CouponEntity.from(coupon));
        return couponEntity.toDomain();
    }

    private CouponPolicy generateTestCouponPolicyFixedType() {
        CouponPolicy couponPolicy = new CouponPolicy(
                null,
                CouponPolicyType.FIXED,
                null,
                LocalDateTime.of(2025, 1, 3, 0, 0, 0),
                LocalDateTime.of(2025, 1, 31, 0, 0, 0)
                );

        CouponPolicyEntity couponPolicyEntity = couponPolicyRepository.save(CouponPolicyEntity.from(couponPolicy));
        return couponPolicyEntity.toDomain();
    }

    @Test
    @DisplayName("[성공] 사용자 쿠폰 사용 테스트 - 사용자가 요청해서 사용 가능한 쿠폰 조회 가능하면 사용 처리 업데이트")
    void useUserCoupon_successTest() {
        // Given
        UserCoupon testUserCoupon = generateTestUserCoupon(null);
        UserCoupon userCoupon = userCouponCommandAdapter.generateUserCoupon(testUserCoupon);

        // When
        userCoupon.updateSpentAt();
        UserCoupon actual = userCouponCommandAdapter.update(userCoupon);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(actual.getSpentAt())
        );
    }

    @Test
    @DisplayName("[성공] 사용자 쿠폰 사용 롤백 테스트 - 사용자 쿠폰 롤백 요청이 들어오면 사용 취소 처리")
    void cancelUserCoupon_successTest() {
        // Given
        UserCoupon testUserCoupon = generateTestUsedUserCoupon();
        UserCoupon userCoupon = userCouponCommandAdapter.generateUserCoupon(testUserCoupon);
        userCoupon.deleteSpentAt();

        // When
        UserCoupon actual = userCouponCommandAdapter.cancelUserCouponUsage(userCoupon);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNull(actual.getSpentAt())
        );
    }

    @Test
    @DisplayName("[실패] 사용자 쿠폰 사용 롤백 테스트 - 조회 불가능한 쿠폰 예외 처리")
    void cancelUserCoupon_failureTest() {
        // Given
        UserCoupon testUserCoupon = generateTestUserCoupon(1L); // DB에 저장 안 한 쿠폰

        // When & Then
        Assertions.assertThrows(
                UserCouponNotFoundException.class,
                () -> userCouponCommandAdapter.cancelUserCouponUsage(testUserCoupon)
        );
    }
}