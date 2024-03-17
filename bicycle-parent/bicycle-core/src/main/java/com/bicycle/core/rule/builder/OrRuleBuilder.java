package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.OrRule;
import com.bicycle.core.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrRuleBuilder implements RuleBuilder {
    
    private final RuleBuilder left, right;

    @Override
    public Rule build() {
        return new OrRule(left.build(), right.build());
    }

    @Override
    public Rule buildDefault() {
        return new OrRule(left.buildDefault(), right.buildDefault());
    }
    
    @Override
    public String toString() {
        return left + " or " + right;
    }

}
