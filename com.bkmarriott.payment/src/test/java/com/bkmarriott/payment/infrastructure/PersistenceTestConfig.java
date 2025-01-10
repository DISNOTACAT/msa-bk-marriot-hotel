package com.bkmarriott.payment.infrastructure;

import com.bkmarriott.payment.infrastructure.persistence.adapter.PaymentCommandAdapter;
import com.bkmarriott.payment.infrastructure.persistence.adapter.PaymentQueryAdapter;
import com.bkmarriott.payment.infrastructure.persistence.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
@Profile("test")
public class PersistenceTestConfig {

  @Bean
  public PaymentQueryAdapter paymentQueryAdapter(@Autowired
      PaymentRepository paymentRepository) {
    return new PaymentQueryAdapter(paymentRepository);
  }

  @Bean
  public PaymentCommandAdapter paymentCommandAdapter(@Autowired
  PaymentRepository paymentRepository) {
    return new PaymentCommandAdapter(paymentRepository);
  }
}
