package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import com.bicycle.Constant;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class WeekdayPostMarketToAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		final DayOfWeek dayOfWeek = date.getDayOfWeek();
		return !Constant.NSE_END_TIME.isAfter(date.toLocalTime()) 
				&& (MONDAY.equals(dayOfWeek)
				|| TUESDAY.equals(dayOfWeek) 
				|| WEDNESDAY.equals(dayOfWeek)
				|| THURSDAY.equals(dayOfWeek)
				|| FRIDAY.equals(dayOfWeek));
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_END_TIME.adjustInto(temporal);
	}
}
