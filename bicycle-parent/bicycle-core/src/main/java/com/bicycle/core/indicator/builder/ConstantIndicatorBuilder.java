package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.util.FloatIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstantIndicatorBuilder implements IndicatorBuilder {
    
    private final FloatIterator floatIterator;
    private final IndicatorCache indicatorCache;

    @Override
    public Indicator build() {
        return indicatorCache.constant(floatIterator.value());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.constant(floatIterator.defaultValue());
    }
    
    @Override
    public String toString() {
        return "const(" + floatIterator.toValueString() + ")";
    }

}
