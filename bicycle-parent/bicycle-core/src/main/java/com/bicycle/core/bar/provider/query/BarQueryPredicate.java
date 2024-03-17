package com.bicycle.core.bar.provider.query;

import java.time.Duration;
import java.util.function.Predicate;

public class BarQueryPredicate implements Predicate<BarQuery> {

	@Override
	public boolean test(BarQuery barQuery) {
	    if(null == barQuery) return false;
		final Duration timeframeDuration = Duration.ofMinutes(barQuery.timeframe().getMinuteMultiple());
		final Duration queryDuration = Duration.between(barQuery.from(), barQuery.to());
		final Duration durationDelta = timeframeDuration.minus(queryDuration);
		return queryDuration.isZero() || 
				durationDelta.isZero() ||
				durationDelta.isNegative();
	}

}
