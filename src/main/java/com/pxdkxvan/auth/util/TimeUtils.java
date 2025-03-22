package com.pxdkxvan.auth.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtils {
    public static final ZonedDateTime MIN = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.systemDefault());
    public static final ZonedDateTime MAX = ZonedDateTime.of(LocalDateTime.MAX, ZoneId.systemDefault());
    public static final BiFunction<ZonedDateTime, TemporalAmount, ZonedDateTime> ahead = ZonedDateTime::plus;
    public static final BiFunction<ZonedDateTime, TemporalAmount, ZonedDateTime> back = ZonedDateTime::minus;
    public static final Function<Instant, ZonedDateTime> toZonedDateTime = instant -> instant.atZone(ZoneId.systemDefault());
}
