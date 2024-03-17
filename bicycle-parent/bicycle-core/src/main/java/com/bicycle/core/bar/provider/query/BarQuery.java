package com.bicycle.core.bar.provider.query;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import java.time.ZonedDateTime;
import lombok.Builder;

@Builder(toBuilder = true)
public record BarQuery(Symbol symbol, ZonedDateTime to, ZonedDateTime from, Timeframe timeframe) {
	
	public BarQuery withTo(ZonedDateTime to) {
		return BarQuery.builder()
				.symbol(symbol)
				.to(to)
				.from(from)
				.timeframe(timeframe)
				.build();
	}

	public BarQuery withFrom(ZonedDateTime from) {
		return BarQuery.builder()
		        .symbol(symbol)
				.to(to)
				.from(from)
				.timeframe(timeframe)
				.build();
	}
	
}
