package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RisingStrengthIndicatorBuilder implements IndicatorBuilder {
    
    private final IndicatorBuilder sourceIndicatorBuilder;
    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.risingStrength(sourceIndicatorBuilder.build(), integerIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.risingStrength(sourceIndicatorBuilder.buildDefault(), integerIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "risingStrength(" + sourceIndicatorBuilder + "," + integerIterator.toValueString() + ")";
    }

}
