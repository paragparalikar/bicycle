package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotRule implements Rule {

    private final Rule delegate;
    
    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        return !delegate.isSatisfied(symbol, timeframe, trade);
    }
    
    @Override
    public float distance(Rule rule) {
        return delegate.distance(NotRule.class.cast(rule).delegate);
    }
    
    @Override
    public String toString() {
        return toText(delegate);
    }
    
    public static String toText(Rule delegate) {
        return "not " + delegate;
    }

}
