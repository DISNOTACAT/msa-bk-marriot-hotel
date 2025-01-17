package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory.InventoryQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.reservation.ReservationCommandAdapter;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.reservation.ReservationQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
@Profile("test")
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
  public InventoryCommandAdaptor inventoryCommandAdaptor(@Autowired InventoryRepository inventoryRepository,InventoryQueryDslRepository inventoryQueryDslRepository) {
    return new InventoryCommandAdaptor(inventoryRepository, inventoryQueryDslRepository);
  }

  @Bean
  public ReservationCommandAdapter reservationCommandAdapter(@Autowired ReservationRepository reservationRepository){
    return new ReservationCommandAdapter(reservationRepository);
  }

  @Bean
  public ReservationQueryAdaptor reservationQueryAdaptor(@Autowired ReservationRepository reservationRepository){
    return new ReservationQueryAdaptor(reservationRepository);
  }
}
