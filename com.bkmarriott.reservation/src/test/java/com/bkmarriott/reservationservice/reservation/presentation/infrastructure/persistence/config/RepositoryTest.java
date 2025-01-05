package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.config.PersistenceConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import(PersistenceConfig.class)
@ActiveProfiles("test")
public @interface RepositoryTest {

}