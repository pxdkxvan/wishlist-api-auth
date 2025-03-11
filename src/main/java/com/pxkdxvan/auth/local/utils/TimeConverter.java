package com.pxkdxvan.auth.local.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConverter {
    public static final BiFunction<ZonedDateTime, TemporalAmount, ZonedDateTime> ahead = ZonedDateTime::plus;
    public static final BiFunction<ZonedDateTime, TemporalAmount, ZonedDateTime> back = ZonedDateTime::minus;
    public static final Function<Instant, ZonedDateTime> toZonedDateTime = instant -> instant.atZone(ZoneId.systemDefault());
}
