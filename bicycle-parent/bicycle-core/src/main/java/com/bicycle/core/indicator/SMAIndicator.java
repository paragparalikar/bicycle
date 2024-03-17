package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.series.SeriesCache;
import com.bicycle.core.indicator.cache.series.SmartSeriesCache;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SMAIndicator implements Indicator {

    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final ValueCache sumCache;
    private final SeriesCache seriesCache;
    
    public SMAIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.sumCache = new SmartValueCache(symbolCount, timeframeCount);
        this.seriesCache = new SmartSeriesCache(barCount + 1, symbolCount, timeframeCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        if(!Float.isNaN(indicatorValue)) {
            
            float sum = sumCache.get(bar.symbol(), bar.timeframe());
            sum = Float.isNaN(sum) ? 0 : sum;
            sum += indicatorValue;
            
            final FloatSeries series = seriesCache.get(bar.symbol(), bar.timeframe());
            series.add(indicatorValue);
            
            float value = Float.NaN;
            if(barCount > series.size()) {
                value = Float.NaN;
            } else if(barCount == series.size()) {
                value = sum / barCount;
            } else {
                sum -= series.get(barCount);
                value = sum / barCount;
            }
            
            sumCache.set(bar.symbol(), bar.timeframe(), sum);
            cache.set(bar.symbol(), bar.timeframe(), value);
        }
    }
    
    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - SMAIndicator.class.cast(other).barCount);
    }
    
    @Override
    public void clear() {
        cache.clear();
        sumCache.clear();
        seriesCache.clear();
    }
    
    @Override
    public String toString() {
        return toText(indicator, barCount);
    }
    
    public static String toText(Indicator indicator, int barCount) {
        return new StringBuilder("sma(")
                .append(indicator.toString())
                .append(",")
                .append(barCount)
                .append(")")
                .toString();
    }
    
}
