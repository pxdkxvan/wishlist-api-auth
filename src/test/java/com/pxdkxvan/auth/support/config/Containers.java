package com.pxdkxvan.auth.support.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static com.pxdkxvan.auth.support.config.DotenvConstants.*;

@Testcontainers
public interface Containers {

    Map<String, String> env = DotenvLoader.loadEnv(String.class);

    @Container
    @ServiceConnection
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(env.getOrDefault(ENV_DB_IMAGE, ENV_DB_IMAGE_DEFAULT))
            .withDatabaseName(env.getOrDefault(ENV_DB_NAME, ENV_DB_NAME_DEFAULT))
            .withUsername(env.getOrDefault(ENV_DB_USERNAME, ENV_DB_USERNAME_DEFAULT))
            .withPassword(env.getOrDefault(ENV_DB_PASSWORD, ENV_DB_PASSWORD_DEFAULT));

}
