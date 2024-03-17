package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.CombinedIndicator;
import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorOperator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CombinedIndicatorBuilder implements IndicatorBuilder {

    private final IndicatorBuilder left, right;
    private final IndicatorOperator operator;
    
    @Override
    public Indicator build() {
        return new CombinedIndicator(left.build(), right.build(), operator);
    }

    @Override
    public Indicator buildDefault() {
        return new CombinedIndicator(left.buildDefault(), right.buildDefault(), operator);
    }

    @Override
    public String toString() {
        return left + " " + operator + " " + right;
    }
    
}
