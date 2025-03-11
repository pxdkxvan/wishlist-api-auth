package com.pxkdxvan.auth.local.aspect.service;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class CaptchaServiceAspect {

    private final DefaultAspectLogger defaultAspectLogger;

}
