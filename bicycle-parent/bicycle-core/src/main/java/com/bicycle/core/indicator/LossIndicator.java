package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LossIndicator implements Indicator {

    private final ValueCache cache;
    private final Indicator indicator;
    private final Indicator previousValueIndicator;
    
    public LossIndicator(int symbolCount, int timeframeCount, Indicator indicator, IndicatorCache indicatorCache) {
        this.indicator = indicator;
        this.previousValueIndicator = indicatorCache.prev(indicator, 1);
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        final float previousIndicatorValue = previousValueIndicator.getValue(bar.symbol(), bar.timeframe());
        float value = Math.max(0, Float.isNaN(previousIndicatorValue) || Float.isNaN(indicatorValue) ? 
                0 : previousIndicatorValue - indicatorValue);
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
        return toText(indicator);
    }
    
    public static String toText(Indicator indicator) {
        return "loss(" + indicator + ")";
    }

}
