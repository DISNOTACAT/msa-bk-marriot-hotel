package com.bkmarriott.payment.payment.infrastructure.persistence.config;

import static com.bkmarriott.payment.payment.presentation.config.HttpHeaderConstants.HEADER_USER_ID;

import com.bkmarriott.payment.payment.presentation.rest.exception.AuthAuditingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaAuditConfig {

  private final HttpServletRequest request;
  private String userId;

  @Bean
  public AuditorAware<Long> auditorProvider() {
    return () -> getCurrentAuditor()
        .map(Long::valueOf);
  }

  public void getUserId(String userId) {
    this.userId = userId;
  }

  private Optional<String> getCurrentAuditor() {
    return Optional.ofNullable(userId).or(this::getHeaderUserId).or(this::throwAuthException);
  }

  private Optional<String> getHeaderUserId() {
    return Optional.ofNullable(request.getHeader(HEADER_USER_ID));
  }

  private Optional<String> throwAuthException() {
    throw new AuthAuditingException("잘못된 요청입니다.");
  }
}
