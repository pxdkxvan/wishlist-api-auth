package com.pxkdxvan.auth.oauth2.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
class RestConfig {
    @Bean
    RestOperations restOperations(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
