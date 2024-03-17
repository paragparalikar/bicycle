package com.bicycle.core.bar.provider;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.symbol.Symbol;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface BarDataProvider {

    List<Bar> get(BarQuery barQuery);

    public default List<Bar> get(Symbol symbol, Timeframe timeframe, int limit) {
        ZonedDateTime to = ZonedDateTime.now();
        ZonedDateTime from = to.minus(Duration.ofMinutes(timeframe.getMinuteMultiple()).multipliedBy(limit));
        final ZonedDateTime maxPastDate = ZonedDateTime.now().minusYears(1);
        final List<Bar> bars = new ArrayList<>(limit);
        do {
            final List<Bar> partialBars = get(BarQuery.builder().symbol(symbol).timeframe(timeframe).to(to).from(from).build());
            if (null == partialBars || partialBars.isEmpty()) {
                to = from.minus(Duration.ofMinutes(timeframe.getMinuteMultiple()));
            } else {
                bars.addAll(partialBars);
                to = ZonedDateTime.ofInstant(Instant.ofEpochMilli(partialBars.get(0).date()), ZoneId.systemDefault())
                        .minus(Duration.ofMinutes(timeframe.getMinuteMultiple()));
            }
            from = to.minus(Duration.ofMinutes(timeframe.getMinuteMultiple()).multipliedBy(limit));
        } while (bars.size() < limit && from.isAfter(maxPastDate));
        return bars.stream().distinct().sorted(Comparator.comparing(Bar::date)).toList();
    }
}
