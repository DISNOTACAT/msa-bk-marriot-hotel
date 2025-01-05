package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class PersistenceConfig {

  @Bean
  public InventoryQueryAdaptor inventoryQueryAdaptor(@Autowired InventoryRepository inventoryRepository) {
    return new InventoryQueryAdaptor(inventoryRepository); // 빈으로 등록
  }

  @Bean
  public InventoryCommandAdaptor inventoryCommandAdaptor(@Autowired InventoryRepository inventoryRepository) {
    return new InventoryCommandAdaptor(inventoryRepository); // 빈으로 등록
  }
}
