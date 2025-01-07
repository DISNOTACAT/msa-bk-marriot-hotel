package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.UserCoupon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "m_user_coupon")
@Entity(name = "UserCoupon")
public class UserCouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponEntity coupon;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime issuanceAt;

    private LocalDateTime spendingAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public UserCouponEntity(Long id, CouponEntity coupon, Long userId, LocalDateTime issuanceAt,
                              LocalDateTime spendingAt, LocalDateTime expiredAt) {
        super.createdByUser(userId);
        this.id = id;
        this.coupon = coupon;
        this.userId = userId;
        this.issuanceAt = issuanceAt;
        this.spendingAt = spendingAt;
        this.expiredAt = expiredAt;
    }

    public UserCoupon toDomain() {
        return new UserCoupon(id, coupon.toDomain(), userId, issuanceAt, spendingAt, expiredAt);
    }

    public static UserCouponEntity from(UserCoupon userCoupon) {
        return new UserCouponEntity(
                userCoupon.getId(),
                CouponEntity.from(userCoupon.getCoupon()),
                userCoupon.getUserId(),
                userCoupon.getIssuedAt(),
                userCoupon.getSpentAt(),
                userCoupon.getExpiredAt()
        );
    }
}