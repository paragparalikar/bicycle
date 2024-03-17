package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AndRule implements Rule {

    private final Rule left, right;
    
    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        return left.isSatisfied(symbol, timeframe, trade) && right.isSatisfied(symbol, timeframe, trade);
    }
    
    @Override
    public float distance(Rule rule) {
        final AndRule andRule = AndRule.class.cast(rule);
        return left.distance(andRule.left) + right.distance(andRule.right);
    }
    
    @Override
    public String toString() {
        return toText(left, right);
    }
    
    public static String toText(Rule left, Rule right) {
        return "(" + left + " and " + right + ")";
    }

}
