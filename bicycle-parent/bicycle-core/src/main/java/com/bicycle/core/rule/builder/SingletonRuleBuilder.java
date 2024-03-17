package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingletonRuleBuilder implements RuleBuilder {
    
    private final Rule rule;

    @Override
    public Rule build() {
        return rule;
    }

    @Override
    public Rule buildDefault() {
        return rule;
    }
    
    @Override
    public String toString() {
        return rule.toString();
    }

}
