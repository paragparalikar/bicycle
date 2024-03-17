package com.bicycle.core.bar.provider.query.time;

import com.bicycle.Constant;
import com.bicycle.core.bar.Timeframe;
import java.time.Duration;
import java.time.ZonedDateTime;

public class BarQueryMarketToAdjuster implements BarQueryMarketTimeAdjuster {

	@Override
	public ZonedDateTime apply(Timeframe timeframe, ZonedDateTime date) {
		final ZonedDateTime startOfMarket = (ZonedDateTime) Constant.NSE_START_TIME.adjustInto(date);
		final Duration duration = Duration.ofMinutes(timeframe.getMinuteMultiple());
		final ZonedDateTime result = startOfMarket
				.plus(duration.multipliedBy(Duration.between(startOfMarket, date).dividedBy(duration)));
		return Constant.NSE_END_TIME.isBefore(date.toLocalTime()) ? result.plus(duration) : result.minus(duration);
	}

}
