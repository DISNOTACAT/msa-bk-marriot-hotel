package com.bkmarriott.coupon.application.service;

import com.bkmarriott.coupon.application.outputport.UserCouponOutputPort;
import com.bkmarriott.coupon.domain.Coupon;
import com.bkmarriott.coupon.domain.CouponPolicy;
import com.bkmarriott.coupon.domain.UserCoupon;
import com.bkmarriott.coupon.domain.vo.CouponPolicyType;
import com.bkmarriott.coupon.domain.vo.UserCouponForIssue;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Application] [Unit] UserCouponService Test")
@ExtendWith(MockitoExtension.class)
public class UserCouponServiceTest {

    private static Coupon TEST_COUPON;

    @InjectMocks
    private UserCouponService userCouponService;

    @Mock private CouponService couponService;
    @Mock private UserCouponOutputPort userCouponOutputPort;

    @BeforeAll
    static void beforeAll() {
        CouponPolicy couponPolicy = new CouponPolicy(
            1L, CouponPolicyType.FIXED, null, LocalDateTime.MIN, LocalDateTime.MAX
        );
        TEST_COUPON = new Coupon(1L, couponPolicy, "test", 10.0f);
    }

    @Test
    @DisplayName("[성공] 쿠폰 발급 테스트 - 쿠폰을 발급한 뒤 UserCoupon 객체를 반환한다.")
    void issueCoupon_successTest() {
        // Given
        LocalDateTime issuedAt = LocalDateTime.now();
        UserCouponForIssue userCouponForIssue = new UserCouponForIssue(1L, 1L, issuedAt);

        Mockito.when(couponService.findCoupon(ArgumentMatchers.anyLong())).thenReturn(TEST_COUPON);
        Mockito.when(userCouponOutputPort.generateUserCoupon(ArgumentMatchers.any(UserCoupon.class)))
            .then(invocation -> invocation.getArgument(0));
        // When
        UserCoupon userCoupon = userCouponService.issueCoupon(userCouponForIssue);
        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(1L, userCoupon.getUserId()),
            () -> Assertions.assertEquals(1L, userCoupon.getCoupon().getId()),
            () -> Assertions.assertEquals(issuedAt, userCoupon.getIssuedAt())
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 사용 테스트 - 쿠폰 사용 시각 업데이트")
    void useUserCoupon_successTest() {
        // Given
        UserCoupon userCoupon = new UserCoupon(1L, TEST_COUPON, 1L, LocalDateTime.now(), null, LocalDateTime.MAX);
        Mockito.when(userCouponOutputPort.findValidCouponById(ArgumentMatchers.anyLong()))
            .thenReturn(userCoupon);
        Mockito.when(userCouponOutputPort.update(ArgumentMatchers.any(UserCoupon.class)))
            .then(invocation -> invocation.getArgument(0));

        // When
        UserCoupon actual = userCouponService.useUserCoupon(userCoupon.getId());
        // Then
        Assertions.assertAll(
            () -> Assertions.assertNotNull(actual.getSpentAt())
        );
    }
}
