package com.bkmarriott.coupon.persistence.config;

import com.bkmarriott.coupon.infrastructure.persistence.config.PersistenceConfig;
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
@Import(PersistenceTestConfig.class)
@ActiveProfiles("test")
public @interface RepositoryTest {

}