package com.bicycle.core.indicator.builder;

import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorOperator;

public interface IndicatorBuilder {

    Indicator build();
    
    Indicator buildDefault();
    
    default IndicatorBuilder plus(IndicatorBuilder other) {
        return new CombinedIndicatorBuilder(this, other, IndicatorOperator.PLUS);
    }
    
    default IndicatorBuilder minus(IndicatorBuilder other) {
        return new CombinedIndicatorBuilder(this, other, IndicatorOperator.MINUS);
    }
    
    default IndicatorBuilder multipliedBy(IndicatorBuilder other) {
        return new CombinedIndicatorBuilder(this, other, IndicatorOperator.MULTIPLIED_BY);
    }
    
    default IndicatorBuilder dividedBy(IndicatorBuilder other) {
        return new CombinedIndicatorBuilder(this, other, IndicatorOperator.DIVIDED_BY);
    }
    
    default IndicatorBuilder pow(IndicatorBuilder other) {
        return new CombinedIndicatorBuilder(this, other, IndicatorOperator.POWER_OF);
    }
}
