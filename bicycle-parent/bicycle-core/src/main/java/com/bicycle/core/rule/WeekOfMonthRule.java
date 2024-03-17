package com.bicycle.core.rule;

import com.bicycle.core.bar.BarSeries;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeekOfMonthRule implements Rule {

    private final IndicatorCache indicatorCache;
    private final Collection<Integer> weeksOfMonth;
    
    @Override
    public float distance(Rule rule) {
        return 0; // Not required
    }

    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        final BarSeries barSeries = indicatorCache.barSeries(symbol, timeframe);
        if(barSeries.isEmpty()) return false;
        final long date = barSeries.get(0).date();
        final Integer weekOfMonth = LocalDate.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).getDayOfMonth() / 7;
        return weeksOfMonth.contains(weekOfMonth);
    }
    
    @Override
    public String toString() {
        return "weekOfMonth(" + weeksOfMonth.stream().map(Object::toString).collect(Collectors.joining(",")) + ")";
    }

}
