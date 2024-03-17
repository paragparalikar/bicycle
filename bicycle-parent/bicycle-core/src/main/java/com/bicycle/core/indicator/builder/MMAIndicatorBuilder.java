package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MMAIndicatorBuilder implements IndicatorBuilder {
    
    private final IndicatorBuilder sourceIndicatorBuilder;
    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.mma(sourceIndicatorBuilder.build(), integerIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.mma(sourceIndicatorBuilder.buildDefault(), integerIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "mma(" + sourceIndicatorBuilder + "," + integerIterator.toValueString() + ")";
    }

}
