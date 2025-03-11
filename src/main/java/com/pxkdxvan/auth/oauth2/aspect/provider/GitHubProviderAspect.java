package com.pxkdxvan.auth.oauth2.aspect.provider;

import com.pxkdxvan.auth.oauth2.exception.ProviderRequestException;
import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.model.Provider;

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
    @AfterThrowing(value = "execution(* com.pxkdxvan.auth.oauth2.provider.GitHubProvider.fetch*(..))", throwing = "e")
    void throwAfterThrowingGitHubProviderFetchMethod(RestClientException e) {
        throw new ProviderRequestException(e.getMessage(), Provider.GITHUB);
    }
}
