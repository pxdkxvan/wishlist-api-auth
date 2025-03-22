package com.pxdkxvan.auth.support.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotenvConstants {
    public static final String ENV_INITIALIZER_PROPERTY = "dotenv";

    public static final String ENV_DB_IMAGE = "JDBC_DATASOURCE_IMAGE";
    public static final String ENV_DB_NAME = "JDBC_DATASOURCE_NAME";
    public static final String ENV_DB_USERNAME = "JDBC_DATASOURCE_USERNAME";
    public static final String ENV_DB_PASSWORD = "JDBC_DATASOURCE_PASSWORD";

    public static final String ENV_DB_IMAGE_DEFAULT = "postgres:15";
    public static final String ENV_DB_NAME_DEFAULT = "postgres";
    public static final String ENV_DB_USERNAME_DEFAULT = "postgres";
    public static final String ENV_DB_PASSWORD_DEFAULT = "postgres";
}
