package com.bkmarriott.coupon.infrastructure.persistence.config;

import com.bkmarriott.coupon.infrastructure.persistence.adapter.MemberCouponQueryAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.repository.MemberCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class PersistenceConfig {

    @Bean
    public MemberCouponQueryAdapter memberCouponQueryAdapter(@Autowired MemberCouponRepository memberCouponRepository) {
        return new MemberCouponQueryAdapter(memberCouponRepository);
    }
}
