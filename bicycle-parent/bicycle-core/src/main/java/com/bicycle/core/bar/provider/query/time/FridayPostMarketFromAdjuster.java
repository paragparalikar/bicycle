package com.bicycle.core.bar.provider.query.time;

import static java.time.DayOfWeek.FRIDAY;
import com.bicycle.Constant;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class FridayPostMarketFromAdjuster implements BarQueryTimeAdjuster {

	@Override
	public boolean test(ZonedDateTime date) {
		return FRIDAY.equals(date.getDayOfWeek()) && date.toLocalTime().isAfter(Constant.NSE_END_TIME);
	}
	
	@Override
	public Temporal adjustInto(Temporal temporal) {
		return Constant.NSE_START_TIME.adjustInto(temporal.plus(Duration.ofDays(3)));
	}

}
