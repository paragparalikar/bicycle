package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;

public class StandardDeviationIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final Indicator varianceIndicator;
    
    public StandardDeviationIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount, IndicatorCache cache) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.varianceIndicator = cache.variance(indicator, barCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float variance = varianceIndicator.getValue(bar.symbol(), bar.timeframe());
        final float value = Float.isNaN(variance) ? Float.NaN : (float) Math.sqrt(variance);
        cache.set(bar.symbol(), bar.timeframe(), value);
    }
    
    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - StandardDeviationIndicator.class.cast(other).barCount);
    }
    
    @Override
    public void clear() {
        cache.clear();
    }
    
    @Override
    public String toString() {
        return toText(indicator, barCount);
    }
    
    public static String toText(Indicator source, int barCount) {
        return "stdDev(" + source + "," + barCount + ")";
    }

}
