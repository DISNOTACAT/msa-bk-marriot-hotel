package com.bkmarriott.coupon.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_coupon_issuance_event_log")
@Entity(name = "CouponIssuanceEventLog")
public class CouponIssuanceEventLogEntity {

    @Id
    private String id;
}
