package com.pxdkxvan.auth.filter;

import com.pxdkxvan.auth.config.ConstantsConfig;

import io.micrometer.common.lang.NonNullApi;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@NonNullApi
public final class CorrelationFilter extends OncePerRequestFilter {

    private static final String CORRELATION_HEADER = ConstantsConfig.CORRELATION_HEADER;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String correlationId = req.getHeader(CORRELATION_HEADER);
        if (correlationId == null || correlationId.isBlank()) correlationId = UUID.randomUUID().toString();

        MDC.put(CORRELATION_HEADER, correlationId);
        resp.setHeader(CORRELATION_HEADER, correlationId);

        try {
            chain.doFilter(req, resp);
        } finally {
            MDC.remove(CORRELATION_HEADER);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        DispatcherType dispatcherType = req.getDispatcherType();
        return dispatcherType == DispatcherType.FORWARD && servletPath.contains("/callback");
    }

}
