package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CCIIndicatorBuilder implements IndicatorBuilder {
    
    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.cci(integerIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.cci(integerIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "cci(" + integerIterator.toValueString() + ")";
    }

}
