package com.bicycle.core.rule.builder;

import com.bicycle.core.indicator.builder.IndicatorBuilder;
import com.bicycle.core.rule.EqualsRule;
import com.bicycle.core.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EqualsRuleBuilder implements RuleBuilder {

    private final IndicatorBuilder left, right;
    
    @Override
    public Rule build() {
        return new EqualsRule(left.build(), right.build());
    }

    @Override
    public Rule buildDefault() {
        return new EqualsRule(left.buildDefault(), right.buildDefault());
    }
    
    @Override
    public String toString() {
        return left + " = " + right;
    }

}
