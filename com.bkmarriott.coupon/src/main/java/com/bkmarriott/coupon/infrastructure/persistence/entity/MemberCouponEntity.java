package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.MemberCoupon;
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
@Table(name = "m_member_coupon")
@Entity(name = "MemberCoupon")
public class MemberCouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponEntity coupon;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime issuanceAt;

    private LocalDateTime spendingAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public MemberCouponEntity(Long id, CouponEntity coupon, Long memberId, LocalDateTime issuanceAt,
                              LocalDateTime spendingAt, LocalDateTime expiredAt) {
        super.createdByUser(memberId);
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
        this.issuanceAt = issuanceAt;
        this.spendingAt = spendingAt;
        this.expiredAt = expiredAt;
    }

    public MemberCoupon toDomain() {
        return new MemberCoupon(id, coupon.toDomain(), memberId, issuanceAt, spendingAt, expiredAt);
    }

    public static MemberCouponEntity from(MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                CouponEntity.from(memberCoupon.getCoupon()),
                memberCoupon.getMemberId(),
                memberCoupon.getIssuanceAt(),
                memberCoupon.getSpendingAt(),
                memberCoupon.getExpiredAt()
        );
    }
}