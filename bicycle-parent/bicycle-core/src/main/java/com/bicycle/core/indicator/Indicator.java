package com.bicycle.core.indicator;

import com.bicycle.core.bar.BarListener;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.rule.EqualsRule;
import com.bicycle.core.rule.GreaterThanRule;
import com.bicycle.core.rule.LesserThanRule;
import com.bicycle.core.rule.Rule;
import com.bicycle.core.symbol.Symbol;

public interface Indicator extends BarListener {
    
    void clear();
    
    default float distance(Indicator other) { return 0; }
    
    float getValue(Symbol symbol, Timeframe timeframe);
    
    default Rule equals(Indicator other) {
        return new EqualsRule(this, other);
    }
    
    default Rule equals(float value) {
        return equals(ConstantIndicator.of(value));
    }
    
    default Rule greaterThan(Indicator other) {
        return new GreaterThanRule(this, other);
    }
    
    default Rule greaterThan(float value) {
        return greaterThan(ConstantIndicator.of(value));
    }
    
    default Rule lesserThan(Indicator other) {
        return new LesserThanRule(this, other);
    }
    
    default Rule lesserThan(float value) {
        return lesserThan(ConstantIndicator.of(value));
    }
    
    default Rule greaterThanOrEquals(Indicator other) {
        return greaterThan(other).or(equals(other));
    }
    
    default Rule greaterThanOrEquals(float value) {
        return greaterThan(value).or(equals(value));
    }
    
    default Rule lesserThanOrEquals(Indicator other) {
        return lesserThan(other).or(equals(other));
    }
    
    default Rule lesserThanOrEquals(float value) {
        return lesserThan(value).or(equals(value));
    }
    
    default Indicator plus(Indicator indicator) {
        return new CombinedIndicator(this, indicator, IndicatorOperator.PLUS);
    }
    
    default Indicator plus(float value) {
        return plus(ConstantIndicator.of(value));
    }
    
    default Indicator minus(Indicator indicator) {
        return new CombinedIndicator(this, indicator, IndicatorOperator.MINUS);
    }
    
    default Indicator minus(float value) {
        return minus(ConstantIndicator.of(value));
    }
    
    default Indicator multipliedBy(Indicator indicator) {
        return new CombinedIndicator(this, indicator, IndicatorOperator.MULTIPLIED_BY);
    }
    
    default Indicator multipliedBy(float value) {
        return multipliedBy(ConstantIndicator.of(value));
    }
    
    default Indicator dividedBy(Indicator indicator) {
        return new CombinedIndicator(this, indicator, IndicatorOperator.DIVIDED_BY);
    }
    
    default Indicator dividedBy(float value) {
        return dividedBy(ConstantIndicator.of(value));
    }
    
    default Indicator pow(Indicator indicator) {
        return new CombinedIndicator(this, indicator, IndicatorOperator.POWER_OF);
    }
    
    default Indicator pow(float value) {
        return pow(ConstantIndicator.of(value));
    }
}
