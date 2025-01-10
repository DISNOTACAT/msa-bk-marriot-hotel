package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignOkHttpConfiguration {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .client(new feign.okhttp.OkHttpClient());
    }
}
