package com.bkmarriott.charge.infrastructure.persistence.config;

import com.bkmarriott.charge.infrastructure.persistence.adapter.RoomChargeAdapter;
import com.bkmarriott.charge.infrastructure.persistence.repository.RoomChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class PersistenceConfig {

    @Bean
    public RoomChargeAdapter roomChargeAdapter(@Autowired RoomChargeRepository roomChargeRepository) {
        return new RoomChargeAdapter(roomChargeRepository);
    }
}
