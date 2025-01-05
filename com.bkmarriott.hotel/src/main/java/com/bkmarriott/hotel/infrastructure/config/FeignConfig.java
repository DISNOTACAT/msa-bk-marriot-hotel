package com.bkmarriott.hotel.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.bkmarriott.hotel.infrastructure.feignClient.client")
public class FeignConfig {
}
