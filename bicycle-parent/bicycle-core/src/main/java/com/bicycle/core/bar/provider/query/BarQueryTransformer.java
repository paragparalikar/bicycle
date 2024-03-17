package com.bicycle.core.bar.provider.query;

import com.bicycle.core.bar.provider.query.time.BarQueryFromAdjuster;
import com.bicycle.core.bar.provider.query.time.BarQueryToAdjuster;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BarQueryTransformer {

	private final BarQueryToAdjuster toAdjuster = new BarQueryToAdjuster();
	private final BarQueryFromAdjuster fromAdjuster = new BarQueryFromAdjuster();
	private final BarQueryPredicate barQueryPredicate = new BarQueryPredicate();
	
	public Optional<BarQuery> transform(@NonNull final BarQuery barQuery){
		final ZonedDateTime to = toAdjuster.adjust(barQuery.to(), barQuery.timeframe());
		final ZonedDateTime from = fromAdjuster.adjust(barQuery.from(), barQuery.timeframe());
		final BarQuery transformedBarQuery = barQuery.withTo(to).withFrom(from);
		return Optional.of(transformedBarQuery).filter(barQueryPredicate);
	}
	
}
