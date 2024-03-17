package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.SATURDAY;
import com.bicycle.Constant;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class SaturdayFromAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		return SATURDAY.equals(date.getDayOfWeek());
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_START_TIME.adjustInto(temporal.plus(Duration.ofDays(2)));
	}

}
