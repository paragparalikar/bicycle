package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HighestValueIndicatorBuilder implements IndicatorBuilder {
    
    private final IndicatorBuilder sourceIndicatorBuilder;
    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.highest(sourceIndicatorBuilder.build(), integerIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.highest(sourceIndicatorBuilder.buildDefault(), integerIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "highest(" + sourceIndicatorBuilder + "," + integerIterator.toValueString() + ")";
    }

}