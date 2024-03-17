package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StopGainRule implements Rule {
    
    private final float atrMultiple;

    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        if(null == trade) return false;
        final float maxAllowedExcurstion = trade.getEntryAtr() * atrMultiple;
        final float atrFavorableExcurstion = trade.getEntryType().multiplier() * (trade.getLtp() - trade.getEntryPrice()) / trade.getEntryAtr();
        if(atrFavorableExcurstion >= maxAllowedExcurstion) {
            trade.setExitPrice(trade.getLtp());
            return true;
        }
        return false;
    }
    
    @Override
    public float distance(Rule rule) {
        return Math.abs(atrMultiple - StopGainRule.class.cast(rule).atrMultiple);
    }
    
    @Override
    public String toString() {
        return "stopGain(" + atrMultiple + ")";
    }

}
