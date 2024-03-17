package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LossIndicatorBuilder implements IndicatorBuilder {

    private final IndicatorBuilder sourceIndicatorBuilder;
    private final IndicatorCache indicatorCache;
    
    @Override
    public Indicator build() {
        return indicatorCache.loss(sourceIndicatorBuilder.build());
    }

    @Override
    public Indicator buildDefault() {
        return indicatorCache.loss(sourceIndicatorBuilder.buildDefault());
    }
    
    @Override
    public String toString() {
        return "loss(" + sourceIndicatorBuilder + ")";
    }

}
