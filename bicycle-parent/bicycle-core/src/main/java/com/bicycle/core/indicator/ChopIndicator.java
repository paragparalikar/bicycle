package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public class ChopIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final SeriesCache seriesCache;
    private final Indicator trIndicator, highestHighIndicator, lowestLowIndicator;
    
    public ChopIndicator(int symbolCount, int timeframeCount, int barCount, IndicatorCache indicatorCache) {
        this.barCount = barCount;
        this.trIndicator = indicatorCache.trueRange();
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.lowestLowIndicator = indicatorCache.lowest(indicatorCache.low(), barCount);
        this.seriesCache = new SmartSeriesCache(barCount + 1, symbolCount, timeframeCount);
        this.highestHighIndicator = indicatorCache.highest(indicatorCache.high(), barCount);
    }

    @Override
    public void onBar(Bar bar) {
        final FloatSeries series = seriesCache.get(bar.symbol(), bar.timeframe());
        series.add(trIndicator.getValue(bar.symbol(), bar.timeframe()));
        float value = 0;
        for(int index = 0; index < barCount; index++) {
            value += series.get(index);
        }
        value /= (highestHighIndicator.getValue(bar.symbol(), bar.timeframe()) - 
                lowestLowIndicator.getValue(bar.symbol(), bar.timeframe()));
        value = (float) (Math.log10(value) / Math.log10(barCount));
        cache.set(bar.symbol(), bar.timeframe(), value);
    }

    @Override
    public void clear() {
        cache.clear();
        seriesCache.clear();
    }

    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - ChopIndicator.class.cast(other).barCount);
    }
    
    @Override
    public String toString() {
        return toText(barCount);
    }
    
    public static String toText(int barCount) {
        return "chop(" + barCount + ")";
    }

}
