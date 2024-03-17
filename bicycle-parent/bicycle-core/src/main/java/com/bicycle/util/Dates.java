package com.bicycle.util;

import com.bicycle.Constant;
import com.bicycle.core.bar.Timeframe;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Dates {

	private Dates() {}
	
	public static ZonedDateTime toZonedDateTime(long value) {
	    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
	}
	
	public long toLong(ZonedDateTime value) {
	    return value.toInstant().toEpochMilli();
	}
	
	public static LocalDateTime toLocalDateTime(long epochMillis) {
		final ZoneId zoneId = ZoneId.systemDefault();
		final Instant instant = Instant.ofEpochMilli(epochMillis);
		return LocalDateTime.ofInstant(instant, zoneId);
	}
	
	public static ZonedDateTime truncate(LocalDateTime timestamp, LocalTime startTime, Duration duration) {
		final Duration timestampDuration = Duration.between(startTime, timestamp.toLocalTime()).abs();
		final LocalTime localTime = startTime.plus(duration.multipliedBy(timestampDuration.dividedBy(duration)));
		return ZonedDateTime.of(timestamp.toLocalDate(), localTime, ZoneId.systemDefault());
	}
	
	public static long ceil(long date, Timeframe timeframe) {
        final long span = timeframe.getMinuteMultiple() * 60000;
        return date + span - (date - Constant.NSE_START_EPOCH) % span;
    }
	
	public static long floor(long date, Timeframe timeframe) {
	    final long span = timeframe.getMinuteMultiple() * 60000;
        return date - (date - Constant.NSE_START_EPOCH) % span;
	}
	
}
