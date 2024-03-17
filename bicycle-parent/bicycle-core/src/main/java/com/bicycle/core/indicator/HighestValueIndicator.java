package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public class HighestValueIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final SeriesCache seriesCache;
    
    public HighestValueIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.seriesCache = new SmartSeriesCache(barCount + 1, symbolCount, timeframeCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        final FloatSeries series = seriesCache.get(bar.symbol(), bar.timeframe());
        series.add(indicatorValue);
        if(!Float.isNaN(indicatorValue)) {
            float highestValue = Float.MIN_VALUE;
            for(int index = 0; index < barCount; index++) {
                highestValue = Math.max(highestValue, series.get(index));
            }
            cache.set(bar.symbol(), bar.timeframe(), highestValue);
        }
    }

    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - HighestValueIndicator.class.cast(other).barCount);
    }
    
    @Override
    public void clear() {
        cache.clear();
        seriesCache.clear();
    }
    
    @Override
    public String toString() {
        return toText(indicator, barCount);
    }
    
    public static String toText(Indicator source, int barCount) {
        return "highest(" + source + "," + barCount + ")";
    }

}
