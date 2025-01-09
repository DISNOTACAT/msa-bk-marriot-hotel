package com.bkmarriott.coupon.persistence.config;


import com.bkmarriott.coupon.application.outputport.CouponOutputPort;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.CouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.CouponEventLogPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponIssuanceEventLogRepository;
import com.bkmarriott.coupon.infrastructure.persistence.repository.CouponRepository;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceTestConfig {

    @Bean
    public UserCouponQueryAdapter userCouponQueryAdapter(@Autowired UserCouponRepository memberCouponRepository) {
        return new UserCouponQueryAdapter(memberCouponRepository);
    }

    @Bean
    public UserCouponCommandPersistenceAdapter userCouponCommandPersistenceAdapter(@Autowired UserCouponRepository memberCouponRepository) {
        return new UserCouponCommandPersistenceAdapter(memberCouponRepository);
    }

    @Bean
    public CouponEventLogPersistenceAdapter couponEventLogPersistenceAdapter(
        @Autowired CouponIssuanceEventLogRepository couponIssuanceEventLogRepository) {

        return new CouponEventLogPersistenceAdapter(couponIssuanceEventLogRepository);
    }

    @Bean
    public CouponCommandPersistenceAdapter couponCommandPersistenceAdapter(
        @Autowired CouponRepository couponRepository) {

        return new CouponCommandPersistenceAdapter(couponRepository);
    }
}
