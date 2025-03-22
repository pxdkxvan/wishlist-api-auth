package com.pxdkxvan.auth.support.config;

import io.micrometer.common.lang.NonNullApi;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import static com.pxdkxvan.auth.support.config.DotenvConstants.ENV_INITIALIZER_PROPERTY;

@NonNullApi
public final class DotenvApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource(ENV_INITIALIZER_PROPERTY, DotenvLoader.loadEnv(Object.class)));
    }
}
