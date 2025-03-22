package com.pxdkxvan.auth.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
class RestConfig {
    @Bean
    RestOperations restOperations(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .errorHandler(handler -> false)
                .build();
    }
}
