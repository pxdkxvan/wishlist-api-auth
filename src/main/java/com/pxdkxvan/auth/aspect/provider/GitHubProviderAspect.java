package com.pxdkxvan.auth.aspect.provider;

import com.pxdkxvan.auth.exception.provider.ProviderRequestException;
import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.model.Provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Aspect
@Component
@Order(AspectPriority.LOWEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class GitHubProviderAspect {
    @AfterThrowing(value = "execution(* com.pxdkxvan.auth.provider.GitHubProvider.fetch*(..))", throwing = "e")
    void throwAfterThrowingGitHubProviderFetchMethod(RestClientException e) {
        throw new ProviderRequestException(e.getMessage(), Provider.GITHUB);
    }
}
