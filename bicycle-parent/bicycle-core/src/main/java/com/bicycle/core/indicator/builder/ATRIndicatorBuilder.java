package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ATRIndicatorBuilder implements IndicatorBuilder {
    
    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.atr(integerIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.atr(integerIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "atr(" + integerIterator.toValueString() + ")";
    }

}
