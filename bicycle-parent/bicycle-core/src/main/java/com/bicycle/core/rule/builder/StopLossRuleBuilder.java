package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.Rule;
import com.bicycle.core.rule.StopLossRule;
import com.bicycle.util.FloatIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StopLossRuleBuilder implements RuleBuilder {
    
    private final FloatIterator floatIterator;

    @Override
    public Rule build() {
        return new StopLossRule(floatIterator.value());
    }

    @Override
    public Rule buildDefault() {
        return new StopLossRule(floatIterator.defaultValue());
    }

    @Override
    public String toString() {
        return "stopLoss(" + floatIterator.toValueString() + ")";
    }
    
}
