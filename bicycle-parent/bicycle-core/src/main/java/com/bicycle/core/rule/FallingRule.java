package com.bicycle.core.rule;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.indicator.PreviousValueIndicator;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;

/**
 * @deprecated Use FallingStrengthIndicator along with GreaterThanRule
 */
@Deprecated
public class FallingRule implements Rule {

    private final int barCount;
    private final float minStrength;
    private final Indicator indicator;
    private final PreviousValueIndicator previousValueIndicator;
    
    public FallingRule(int barCount, float minStrength, Indicator indicator, IndicatorCache indicatorCache) {
        this.barCount = barCount;
        this.minStrength = minStrength;
        this.indicator = indicator;
        this.previousValueIndicator = (PreviousValueIndicator) indicatorCache.prev(indicator, barCount);
    }
    
    @Override
    public boolean isSatisfied(Symbol symbol, Timeframe timeframe, Position trade) {
        int count = 0;
        for(int index = 0; index < barCount; index++) {
            final float value = previousValueIndicator.getValue(index, symbol, timeframe);
            final float previousValue = previousValueIndicator.getValue(index + 1, symbol, timeframe);
            if(value < previousValue) {
                count++;
            }
        }
        return minStrength <= ((float)count) / ((float)barCount);
    }

    @Override
    public float distance(Rule rule) {
        final FallingRule fallingRule = FallingRule.class.cast(rule);
        return Math.abs(barCount - fallingRule.barCount) + Math.abs(minStrength - fallingRule.minStrength)
            + indicator.distance(fallingRule.indicator);
    }
    
    @Override
    public String toString() {
        return "(" + indicator + " falling " + barCount + "," + minStrength + ")";
    }
    
}
