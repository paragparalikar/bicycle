package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public class FallingStrengthIndicator implements Indicator {

    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final SeriesCache seriesCache;
    
    public FallingStrengthIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount) {
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
        if(barCount >= series.size()) {
            cache.set(bar.symbol(), bar.timeframe(), Float.NaN);
        } else {
            float count = 0;
            for(int index = 0; index < barCount; index++) {
                count += series.get(index) < series.get(index + 1) ? 1 : 0;
            }
            cache.set(bar.symbol(), bar.timeframe(), count / barCount);
        }
    }

    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - FallingStrengthIndicator.class.cast(other).barCount);
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
    
    public static String toText(Indicator indicator, int barCount) {
        return "falling-strength(" + indicator + "," + barCount + ")";
    }

}
