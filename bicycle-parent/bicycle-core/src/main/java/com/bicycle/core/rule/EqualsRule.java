package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.Numbers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EqualsRule implements Rule {

    private final Indicator first, second;
    
    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        
        final float firstValue = first.getValue(symbol, timeframe);
        if(Float.isNaN(firstValue)) return false;
        
        final float secondValue = second.getValue(symbol, timeframe);
        if(Float.isNaN(secondValue)) return false;
        
        return Numbers.truncate(firstValue, 3) == Numbers.truncate(secondValue, 3);
    }
    
    @Override
    public float distance(Rule rule) {
        final EqualsRule equalsRule = EqualsRule.class.cast(rule);
        return first.distance(equalsRule.first) + second.distance(equalsRule.second);
    }
    
    @Override
    public String toString() {
        return toText(first, second);
    }
    
    public static String toText(Indicator left, Indicator right) {
        return "(" + left + " = " + right + ")";
    }

}
