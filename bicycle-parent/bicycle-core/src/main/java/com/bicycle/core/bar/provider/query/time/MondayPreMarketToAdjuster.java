package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.MONDAY;
import com.bicycle.Constant;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class MondayPreMarketToAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		return MONDAY.equals(date.getDayOfWeek()) && Constant.NSE_START_TIME.isAfter(date.toLocalTime());
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_END_TIME.adjustInto(temporal).minus(Duration.ofDays(3));
	}

}
