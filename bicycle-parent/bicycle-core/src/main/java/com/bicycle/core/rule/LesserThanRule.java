package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LesserThanRule implements Rule {

    private final Indicator first, second;
    
    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        
        final float firstValue = first.getValue(symbol, timeframe);
        if(Float.isNaN(firstValue)) return false;
        
        final float secondValue = second.getValue(symbol, timeframe);
        if(Float.isNaN(secondValue)) return false;
        
        return firstValue < secondValue;
    }
    
    @Override
    public float distance(Rule rule) {
        final LesserThanRule other = LesserThanRule.class.cast(rule);
        return first.distance(other.first) + second.distance(other.second);
    }
    
    @Override
    public String toString() {
        return toText(first, second);
    }
    
    public static String toText(Indicator left, Indicator right) {
        return "(" + left + " < " + right + ")";
    }

}
