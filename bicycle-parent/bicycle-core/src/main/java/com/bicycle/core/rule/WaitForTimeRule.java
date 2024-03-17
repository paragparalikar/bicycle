package com.bicycle.core.rule;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarSeries;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WaitForTimeRule implements Rule {
    
    private final long time;
    private final IndicatorCache indicatorCache;

    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        final BarSeries barSeries = indicatorCache.barSeries(symbol, timeframe);
        for(int entryIndex = 0; entryIndex < barSeries.size(); entryIndex++) {
            final Bar entryBar = barSeries.get(entryIndex);
            if(entryBar.date() == trade.getEntryDate()) {
                final Bar currentBar = barSeries.get(0);
                if(currentBar.date() - entryBar.date() >= time) return true;
            }
        }
        return false;
    }
    
    @Override
    public float distance(Rule rule) {
        return Math.abs(time - WaitForTimeRule.class.cast(rule).time);
    }
    
    @Override
    public String toString() {
        return "after " + time + " millis";
    }

}
