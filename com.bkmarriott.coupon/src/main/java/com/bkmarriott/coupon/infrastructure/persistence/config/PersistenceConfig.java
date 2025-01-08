package com.bkmarriott.coupon.infrastructure.persistence.config;

import com.bkmarriott.coupon.infrastructure.persistence.adapter.UserCouponCommandPersistenceAdapter;
import com.bkmarriott.coupon.infrastructure.persistence.repository.UserCouponRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableJpaAuditing
@Configuration
public class PersistenceConfig {

    @Bean
    public UserCouponCommandPersistenceAdapter userCouponCommandPersistenceAdapter(@Autowired UserCouponRepository userCouponRepository) {
        return new UserCouponCommandPersistenceAdapter(userCouponRepository);
    }
}
