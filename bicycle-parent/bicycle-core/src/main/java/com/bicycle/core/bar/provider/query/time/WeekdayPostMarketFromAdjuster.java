package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import com.bicycle.Constant;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class WeekdayPostMarketFromAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		final LocalTime localTime = date.toLocalTime();
		final DayOfWeek dayOfWeek = date.getDayOfWeek();
		return (Constant.NSE_END_TIME.equals(localTime) 
				|| Constant.NSE_END_TIME.isBefore(localTime))
				&& (MONDAY.equals(dayOfWeek)
				|| TUESDAY.equals(dayOfWeek) 
				|| WEDNESDAY.equals(dayOfWeek)
				|| THURSDAY.equals(dayOfWeek));
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_START_TIME.adjustInto(temporal.plus(Duration.ofDays(1)));
	}

}
