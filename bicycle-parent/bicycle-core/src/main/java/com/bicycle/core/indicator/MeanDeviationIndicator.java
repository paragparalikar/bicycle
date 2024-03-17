package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public class MeanDeviationIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final Indicator smaIndicator;
    private final SeriesCache seriesCache;
    
    public MeanDeviationIndicator(int symbolCount, int timeframeCount, int barCount, Indicator indicator, IndicatorCache indicatorCache) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.smaIndicator = indicatorCache.sma(indicator, barCount);
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.seriesCache = new SmartSeriesCache(barCount + 1, symbolCount, timeframeCount);
    }

    @Override
    public void onBar(Bar bar) {
        final float smaValue = smaIndicator.getValue(bar.symbol(), bar.timeframe());
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        
        final FloatSeries series = seriesCache.get(bar.symbol(), bar.timeframe());
        series.add(indicatorValue);
        
        float value = Float.NaN;
        if(Float.isNaN(indicatorValue) || Float.isNaN(smaValue) || barCount > series.size()) {
            value = Float.NaN;
        } else {
            double absoluteDeviations = 0;
            for(int index = 0; index < barCount; index++) {
                absoluteDeviations += Math.abs(smaValue - series.get(index));
            }
            value = (float)(absoluteDeviations / barCount);
        }
        cache.set(bar.symbol(), bar.timeframe(), value);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - MeanDeviationIndicator.class.cast(other).barCount);
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
    public String toString() {
        return toText(barCount, indicator);
    }
    
    public static String toText(int barCount, Indicator indicator) {
        return "meanDev(" + indicator + "," + barCount + ")";
    }

}
