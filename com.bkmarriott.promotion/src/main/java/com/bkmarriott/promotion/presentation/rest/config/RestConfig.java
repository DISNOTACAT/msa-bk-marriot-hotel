package com.bkmarriott.promotion.presentation.rest.config;

import com.bkmarriott.promotion.presentation.rest.util.auth.LoginActorArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class RestConfig implements WebMvcConfigurer {

    private final LoginActorArgumentResolver loginActorArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginActorArgumentResolver);
    }
}
