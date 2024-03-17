package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.SUNDAY;
import com.bicycle.Constant;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class SundayToAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		return SUNDAY.equals(date.getDayOfWeek());
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_END_TIME.adjustInto(temporal).minus(Duration.ofDays(2));
	}

}
