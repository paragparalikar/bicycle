package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstantIndicator implements Indicator {
    
    public static ConstantIndicator of(float value) {
        return new ConstantIndicator(value);
    }
    
    @Getter private final float value;
    
    @Override
    public float getValue(Symbol symbol, Timeframe timeframe) {
        return value;
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(value - ConstantIndicator.class.cast(other).value);
    }
    
    @Override
    public void onBar(Bar bar) { }
    
    @Override
    public void clear() { }
    
    @Override
    public String toString() {
        return toText(value);
    }
    
    public static String toText(float value) {
        return String.valueOf(value);
    }

}
