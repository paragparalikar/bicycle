package com.bicycle.core.rule.builder;

import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.rule.Rule;
import com.bicycle.core.rule.WaitForBarCountRule;
import com.bicycle.util.IntegerIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WaitForBarCountRuleBuilder implements RuleBuilder {

    private final IntegerIterator integerIterator;
    private final IndicatorCache indicatorCache;
    
    @Override
    public Rule build() {
        return new WaitForBarCountRule(integerIterator.value(), indicatorCache);
    }

    @Override
    public Rule buildDefault() {
        return new WaitForBarCountRule(integerIterator.defaultValue(), indicatorCache);
    }
    
    @Override
    public String toString() {
        return "after " + integerIterator.toValueString() + " bars";
    }

}
