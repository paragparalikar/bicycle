package com.bicycle.core.bar.provider.query.time;

import com.bicycle.core.bar.Timeframe;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class BarQueryToAdjuster {

	private final BarQueryMarketTimeAdjuster marketTimeAdjuster = new BarQueryMarketToAdjuster();
	private final List<BarQueryTimeAdjuster> adjusters = Arrays.asList(
			new MondayPreMarketToAdjuster(),
			new SaturdayToAdjuster(),
			new SundayToAdjuster(),
			new WeekdayPostMarketToAdjuster(),
			new WeekdayPreMarketToAdjuster());
	
	public ZonedDateTime adjust(ZonedDateTime date, Timeframe timeframe) {
		if(Timeframe.D.equals(timeframe)) return date.truncatedTo(ChronoUnit.DAYS);
		for(BarQueryTimeAdjuster adjuster : adjusters) {
			if(adjuster.test(date)) {
				date = (ZonedDateTime) adjuster.adjustInto(date);
			}
		}
		return marketTimeAdjuster.apply(timeframe, date);
	}
}
