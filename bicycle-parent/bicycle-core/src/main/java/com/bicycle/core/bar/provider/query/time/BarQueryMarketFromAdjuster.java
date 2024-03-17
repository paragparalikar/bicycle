package com.bicycle.core.bar.provider.query.time;

import com.bicycle.Constant;
import com.bicycle.core.bar.Timeframe;
import java.time.Duration;
import java.time.ZonedDateTime;

public class BarQueryMarketFromAdjuster implements BarQueryMarketTimeAdjuster {

	@Override
	public ZonedDateTime apply(Timeframe timeframe, ZonedDateTime date) {
		final ZonedDateTime startOfMarket = (ZonedDateTime) Constant.NSE_START_TIME.adjustInto(date);
		final Duration duration = Duration.ofMinutes(timeframe.getMinuteMultiple());
		return startOfMarket.plus(duration.multipliedBy(Duration.between(startOfMarket, date).dividedBy(duration)));
	}
	
}
