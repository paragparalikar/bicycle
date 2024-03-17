package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;

public class TRIndicator implements Indicator {
    
    private final ValueCache cache;
    private final Indicator previousClosePriceIndicator;
    
    public TRIndicator(int symbolCount, int timeframeCount, IndicatorCache indicatorCache) {
        cache = new SmartValueCache(symbolCount, timeframeCount);
        previousClosePriceIndicator = indicatorCache.prev(indicatorCache.close(), 1);
    }

    @Override
    public void onBar(Bar bar) {
        final float hl = bar.high() - bar.low();
        final float previousBarClose = previousClosePriceIndicator.getValue(bar.symbol(), bar.timeframe());
        float value = hl;
        if(!Float.isNaN(previousBarClose)) {
            final float hc = bar.high() - previousBarClose;
            final float cl = previousBarClose - bar.low();
            value = Math.max(Math.abs(hl), Math.max(Math.abs(hc), Math.abs(cl)));
        }
        cache.set(bar.symbol(), bar.timeframe(), value);
    }
    
    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public void clear() {
        cache.clear();
    }
    
    @Override
    public String toString() {
        return toText();
    }
    
    public static String toText() {
        return "trueRange";
    }

}
