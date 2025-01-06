package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class PersistenceTestConfig {

  @Autowired
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  public InventoryQueryDslRepository inventoryQueryDslRepository() {
    return new InventoryQueryDslRepository(jpaQueryFactory(entityManager));
  }

  @Bean
  public InventoryQueryAdaptor inventoryQueryAdaptor(@Autowired InventoryRepository inventoryRepository,
      InventoryQueryDslRepository inventoryQueryDslRepository) {
    return new InventoryQueryAdaptor(inventoryRepository, inventoryQueryDslRepository);
  }

  @Bean
  public InventoryCommandAdaptor inventoryCommandAdaptor(@Autowired InventoryRepository inventoryRepository) {
    return new InventoryCommandAdaptor(inventoryRepository);
  }
}
