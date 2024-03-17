package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;

public interface Rule {
    
    float distance(Rule rule);
    
    boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade);
    
    default Rule and(Rule other) {
        return new AndRule(this, other);
    }
    
    default Rule or(Rule other) {
        return new OrRule(this, other);
    }
    
    default Rule not() {
        return new NotRule(this);
    }
    
}
