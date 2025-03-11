package com.pxkdxvan.auth;

import com.pxkdxvan.auth.local.properties.CaptchaProperties;
import com.pxkdxvan.auth.local.properties.CleanerProperties;
import com.pxkdxvan.auth.shared.properties.JwtProperties;
import com.pxkdxvan.auth.local.properties.MailOptionsProperties;
import com.pxkdxvan.auth.local.properties.MailProperties;
import com.pxkdxvan.auth.shared.properties.VerificationProperties;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties({
        JwtProperties.class,
        VerificationProperties.class,
        MailProperties.class,
        MailOptionsProperties.class,
        CleanerProperties.class,
        CaptchaProperties.class
})
public class TaskManagerApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }

}
