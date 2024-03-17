package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.AndRule;
import com.bicycle.core.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AndRuleBuilder implements RuleBuilder {
    
    private final RuleBuilder left, right;

    @Override
    public Rule build() {
        return new AndRule(left.build(), right.build());
    }

    @Override
    public Rule buildDefault() {
        return new AndRule(left.buildDefault(), right.buildDefault());
    }
    
    @Override
    public String toString() {
        return left + " and " + right;
    }

}
