package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public class PreviousValueIndicator implements Indicator {

    private final int barCount;
    private final SeriesCache cache;
    private final Indicator indicator;
    
    public PreviousValueIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.cache = new SmartSeriesCache(barCount + 1, symbolCount, timeframeCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        final FloatSeries series = cache.get(bar.symbol(), bar.timeframe());
        series.add(indicatorValue);
    }

    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        final FloatSeries series = cache.get(symbol, timeframe);
        return barCount >= series.size() ? Float.NaN : series.get(barCount);
    }
    
    public float getValue(int index, Symbol symbol, Timeframe timeframe) {
        final FloatSeries series = cache.get(symbol, timeframe);
        return index >= series.size() ? Float.NaN : series.get(index);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - PreviousValueIndicator.class.cast(other).barCount);
    }
    
    @Override
    public void clear() {
        cache.clear();
    }
    
    @Override
    public String toString() {
        return toText(indicator, barCount);
    }
    
    public static String toText(Indicator indicator, int barCount) {
        return "previous(" + indicator + "," + barCount + ")";
    }

}
