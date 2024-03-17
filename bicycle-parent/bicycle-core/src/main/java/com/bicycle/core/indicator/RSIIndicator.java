package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RSIIndicator implements Indicator {
    
    private final int barCount;
    private final ValueCache cache;
    private final Indicator indicator;
    private final Indicator averageGainIndicator;
    private final Indicator averageLossIndicator;

    public RSIIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount, IndicatorCache indicatorCache) {
        this.barCount = barCount;
        this.indicator = indicator;
        this.cache = new SmartValueCache(symbolCount, timeframeCount);
        this.averageGainIndicator = indicatorCache.mma(indicatorCache.gain(indicator), barCount);
        this.averageLossIndicator = indicatorCache.mma(indicatorCache.loss(indicator), barCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        float value = Float.NaN;
        final float averageGain = averageGainIndicator.getValue(bar.symbol(), bar.timeframe());
        final float averageLoss = averageLossIndicator.getValue(bar.symbol(), bar.timeframe());
        if(Float.isNaN(averageGain) || Float.isNaN(averageLoss)) {
            value = Float.NaN;
        } else if(0 == averageLoss) {
            value = 0 == averageGain ? 0 : 100;
        } else {
            value = 100 - (100 / (1 + averageGain / averageLoss));
        }
        cache.set(bar.symbol(), bar.timeframe(), value);
    }
    
    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return cache.get(symbol, timeframe);
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - RSIIndicator.class.cast(other).barCount);
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
        return new StringBuilder("rsi(")
                .append(indicator.toString())
                .append(",")
                .append(barCount)
                .append(")")
                .toString();
    }
    
}
