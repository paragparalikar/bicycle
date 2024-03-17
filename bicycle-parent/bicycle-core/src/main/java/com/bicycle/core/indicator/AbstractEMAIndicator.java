package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.cache.value.SmartValueCache;
import com.bicycle.core.indicator.cache.value.ValueCache;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractEMAIndicator implements Indicator {

    private final ValueCache cache;
    private final Indicator indicator;
    private final Indicator multiplierIndicator;
    
    public AbstractEMAIndicator(int symbolCount, int timeframeCount, Indicator indicator, Indicator multiplierIndicator) {
        this.indicator = indicator;
        this.multiplierIndicator = multiplierIndicator;
        cache = new SmartValueCache(symbolCount, timeframeCount);
    }
    
    @Override
    public void onBar(Bar bar) {
        final float indicatorValue = indicator.getValue(bar.symbol(), bar.timeframe());
        final float multiplierValue = multiplierIndicator.getValue(bar.symbol(), bar.timeframe());
        
        float value = cache.get(bar.symbol(), bar.timeframe());
        
        if(!Float.isNaN(indicatorValue) && !Float.isNaN(multiplierValue)) {
            if(Float.isNaN(value)){
                value = indicatorValue * multiplierValue;
            } else {
                value = value + (indicatorValue - value) * multiplierValue;
            }
        }
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
    
}
