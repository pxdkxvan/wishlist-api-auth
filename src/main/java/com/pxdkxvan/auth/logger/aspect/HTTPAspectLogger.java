package com.pxdkxvan.auth.logger.aspect;

import com.pxdkxvan.auth.logger.LoggerBuilder;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class HTTPAspectLogger extends DefaultAspectLogger {

    private final HttpServletRequest request;

    @Override
    protected LoggerBuilder.PrefixedHeaderStep logHeader(LoggerBuilder.Prefix prefix) {
        return LoggerBuilder
                .builder()
                .withPrefix(prefix)
                .withItem(request.getMethod())
                .withItem(request.getRequestURI())
                .withItem(request.getRemoteAddr());
    }

}
