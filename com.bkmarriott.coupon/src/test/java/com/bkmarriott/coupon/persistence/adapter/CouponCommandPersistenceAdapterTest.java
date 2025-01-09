package com.bkmarriott.coupon.persistence.adapter;

import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.CouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponEntity;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntity;
import com.bkmarriott.coupon.infrastructure.persistence.entity.CouponPolicyEntityType;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponPolicyRepository;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponRepository;
import com.bkmarriott.coupon.persistence.config.RepositoryTest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] [Integration] CouponCommandPersistenceAdapter Test")
@RepositoryTest
class CouponCommandPersistenceAdapterTest {

    @Autowired
    private CouponCommandPersistenceAdapter couponAdapter;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    private CouponEntity testCouponEntity;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
        couponPolicyRepository.deleteAll();

        CouponPolicyEntity couponPolicyEntity = new CouponPolicyEntity(null, CouponPolicyEntityType.AFTER, 10, null, null);
        couponPolicyEntity = couponPolicyRepository.save(couponPolicyEntity);

        CouponEntity couponEntity = new CouponEntity(null, couponPolicyEntity, "test", 10f);
        testCouponEntity = couponRepository.save(couponEntity);
    }

    @Test
    @DisplayName("[성공] 아이디로 조회 테스트 - 일치하는 아이디가 존재하는 경우 도메인 객체를 Optional에 담아 반환")
    void findById_successTest_matchedId() {
        // Given
        Long couponId = testCouponEntity.getId();
        // When
        Optional<Coupon> actual = couponAdapter.findById(couponId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertTrue(actual.isPresent())
        );
    }

    @Test
    @DisplayName("[성공] 아이디로 조회 테스트 - 일치하는 아이디가 존재하지 않는 경우 빈 Optional 객체 반환")
    void findById_successTest_notMatchedId() {
        // Given
        Long notMatchedCouponId = -1L;
        // When
        Optional<Coupon> actual = couponAdapter.findById(notMatchedCouponId);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertFalse(actual.isPresent())
        );
    }
}