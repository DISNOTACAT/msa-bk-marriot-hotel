package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntity;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntityType;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponPolicyRepository;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponRepository;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] [Integration] UserCouponCommandPersistenceAdapter Test")
@RepositoryTest
public class UserCouponCommandPersistenceAdapterTest {

    @Autowired
    private UserCouponCommandPersistenceAdapter userCouponAdapter;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        CouponPolicyEntity couponPolicyEntity = new CouponPolicyEntity(null, CouponPolicyEntityType.AFTER, 10, null, null);
        couponPolicyEntity = couponPolicyRepository.save(couponPolicyEntity);

        CouponEntity couponEntity = new CouponEntity(null, couponPolicyEntity, "test", 10f);
        couponEntity = couponRepository.save(couponEntity);

        coupon = couponEntity.toDomain();
    }

    @Test
    @DisplayName("[성공] 유저 쿠폰 생성 테스트 - 유효한 정보가 주어진 경우 엔티티를 생성하고 도메인을 반환")
    void generateUserCoupon_successTest() {
        // Given
        LocalDateTime issueAt = LocalDateTime.now();
        LocalDateTime expireTime = coupon.calcCouponExpireTime(issueAt);
        UserCoupon userCoupon = new UserCoupon(null, coupon, 1L, issueAt, null, expireTime);
        // When
        UserCoupon actual = userCouponAdapter.generateUserCoupon(userCoupon);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(userCoupon.getUserId(), actual.getUserId()),
            () -> Assertions.assertEquals(userCoupon.getCoupon().getId(), actual.getCoupon().getId()),
            () -> Assertions.assertEquals(userCoupon.getExpiredAt(), actual.getExpiredAt())
        );
    }

    @Test
    @DisplayName("[성공] 유저 쿠폰 조회 테스트 - 사용가능한 쿠폰을 조회한 뒤 엔티티를 반환")
    void findValidCouponById_successTest() {
        // Given
        LocalDateTime issueAt = LocalDateTime.now();
        LocalDateTime expireTime = coupon.calcCouponExpireTime(issueAt);
        UserCoupon userCoupon = new UserCoupon(null, coupon, 1L, issueAt, null, expireTime);
        UserCoupon save = userCouponAdapter.generateUserCoupon(userCoupon);
        // When
        UserCoupon actual = userCouponAdapter.findValidCouponById(save.getId());
        // Then
        Assertions.assertAll(
            () -> Assertions.assertNotNull(actual),
            () -> Assertions.assertEquals(save.getId(), actual.getId()),
            () -> Assertions.assertNull(save.getSpentAt())
        );
    }
}
