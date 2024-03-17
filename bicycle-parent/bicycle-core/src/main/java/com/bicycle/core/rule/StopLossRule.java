package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StopLossRule implements Rule {
    
    private final float atrMultiple;

    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        if(null == trade) return false;
        final float maxAllowedExcurstion = trade.getEntryAtr() * atrMultiple;
        final float atrAdverseExcurstion = trade.getEntryType().multiplier() * (trade.getEntryPrice() - trade.getLtp()) / trade.getEntryAtr();
        if(atrAdverseExcurstion >= maxAllowedExcurstion) {
            trade.setExitPrice(trade.getLtp());
            return true;
        }
        return false;
    }
    
    @Override
    public float distance(Rule rule) {
        return Math.abs(atrMultiple - StopLossRule.class.cast(rule).atrMultiple);
    }
    
    @Override
    public String toString() {
        return "stopLoss(" + atrMultiple + ")";
    }

}
