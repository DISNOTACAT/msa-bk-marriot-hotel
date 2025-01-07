package com.bkmarriott.coupon.persistence.config;


import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
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
}
