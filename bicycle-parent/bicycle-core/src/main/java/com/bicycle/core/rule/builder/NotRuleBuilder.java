package com.bicycle.core.rule.builder;

import com.bicycle.core.rule.NotRule;
import com.bicycle.core.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotRuleBuilder implements RuleBuilder {
    
    private final RuleBuilder delegate;

    @Override
    public Rule build() {
        return new NotRule(delegate.build());
    }

    @Override
    public Rule buildDefault() {
        return new NotRule(delegate.buildDefault());
    }
    
    @Override
    public String toString() {
        return "not " + delegate;
    }

}
