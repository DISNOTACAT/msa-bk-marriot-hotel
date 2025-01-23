package com.bkmarriott.coupon.infrastructure.persistence.entity;

import com.bkmarriott.coupon.domain.Coupon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "m_coupon")
@Entity(name = "Coupon")
public class CouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicyEntity couponPolicy;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float discountRate;

    public CouponEntity(Long id, CouponPolicyEntity couponPolicy, String name, Float discountRate) {
        super.createdBySystem();
        this.id = id;
        this.couponPolicy = couponPolicy;
        this.name = name;
        this.discountRate = discountRate;
    }

    public Coupon toDomain() {
        return new Coupon(id, couponPolicy.toDomain(), name, discountRate);
    }

    public static CouponEntity from(Coupon coupon) {
        return new CouponEntity(
            coupon.getId(),
            CouponPolicyEntity.generateWithId(coupon.getCouponPolicy().getId()),
            coupon.getName(),
            coupon.getDiscountRate()
        );
    }
}
