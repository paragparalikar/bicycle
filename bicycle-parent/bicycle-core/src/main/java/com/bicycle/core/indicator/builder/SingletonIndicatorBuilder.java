package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingletonIndicatorBuilder implements IndicatorBuilder {

    private final Indicator indicator;
    
    @Override
    public Indicator build() {
        return indicator;
    }
    
    @Override
    public Indicator buildDefault() {
        return indicator;
    }
    
    @Override
    public String toString() {
        return indicator.toString();
    }

}
