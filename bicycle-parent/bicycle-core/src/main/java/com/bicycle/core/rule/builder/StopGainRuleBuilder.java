package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.Rule;
import com.bicycle.core.rule.StopGainRule;
import com.bicycle.util.FloatIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StopGainRuleBuilder implements RuleBuilder {
    
    private final FloatIterator floatIterator;

    @Override
    public Rule build() {
        return new StopGainRule(floatIterator.value());
    }

    @Override
    public Rule buildDefault() {
        return new StopGainRule(floatIterator.defaultValue());
    }

    @Override
    public String toString() {
        return "stopGain(" + floatIterator.toValueString() + ")";
    }
    
}
