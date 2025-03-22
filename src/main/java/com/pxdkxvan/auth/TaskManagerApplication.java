package com.pxdkxvan.auth;

import com.pxdkxvan.auth.properties.CleanerProperties;
import com.pxdkxvan.auth.properties.JwtProperties;
import com.pxdkxvan.auth.properties.MailOptionsProperties;
import com.pxdkxvan.auth.properties.MailProperties;
import com.pxdkxvan.auth.properties.VerificationProperties;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@EnableConfigurationProperties({JwtProperties.class, VerificationProperties.class,
        MailProperties.class, MailOptionsProperties.class, CleanerProperties.class})
public class TaskManagerApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }

}
