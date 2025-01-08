package com.bkmarriott.promotion.infrastructure.persistence.entity;

import com.bkmarriott.promotion.domain.Promotion;
import com.bkmarriott.promotion.domain.vo.PromotionPeriod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "m_promotion")
@Entity(name = "promotion")
public class PromotionEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer maxIssuance;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    public Promotion toDomain() {
        PromotionPeriod promotionPeriod = PromotionPeriod.valueOf(startedAt, endedAt);
        return new Promotion(promotionId, couponId, name, description, maxIssuance, promotionPeriod);
    }
}
