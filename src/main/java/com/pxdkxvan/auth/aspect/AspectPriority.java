package com.pxdkxvan.auth.aspect;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AspectPriority {
    public static final int HIGHEST = -100;
    public static final int HIGH = -50;
    public static final int MEDIUM = 0;
    public static final int LOW = 50;
    public static final int LOWEST = 100;
}
