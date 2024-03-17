package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GainIndicatorBuilder implements IndicatorBuilder {

    private final IndicatorBuilder sourceIndicatorBuilder;
    private final IndicatorCache indicatorCache;
    
    @Override
    public Indicator build() {
        return indicatorCache.gain(sourceIndicatorBuilder.build());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.gain(sourceIndicatorBuilder.buildDefault());
    }
    
    @Override
    public String toString() {
        return "gain(" + sourceIndicatorBuilder + ")";
    }

}
