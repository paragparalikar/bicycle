package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;

public class CCIIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final Indicator typicalPriceIndicator, smaIndicator, meanDeviationIndicator;

    public CCIIndicator(int symbolCount, int timeframeCount, int barCount, IndicatorCache indicatorCache) {
        this.barCount = barCount;
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.typicalPriceIndicator = indicatorCache.typicalPrice();
        this.smaIndicator = indicatorCache.sma(typicalPriceIndicator, barCount);
        this.meanDeviationIndicator = indicatorCache.meanDev(typicalPriceIndicator, barCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float meanDeviation = meanDeviationIndicator.getValue(bar.symbol(), bar.timeframe());
        if(0 == meanDeviation) {
            cache.set(bar.symbol(), bar.timeframe(), 0);
        } else {
            final float value = (typicalPriceIndicator.getValue(bar.symbol(), bar.timeframe())
                    - smaIndicator.getValue(bar.symbol(), bar.timeframe()))
                    / meanDeviation * 0.015f;
            cache.set(bar.symbol(), bar.timeframe(), value);
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - CCIIndicator.class.cast(other).barCount);
    }
    
    @Override
    public String toString() {
        return toText(barCount);
    }
    
    public static String toText(int barCount) {
        return "cci(" + barCount + ")";
    }

}
