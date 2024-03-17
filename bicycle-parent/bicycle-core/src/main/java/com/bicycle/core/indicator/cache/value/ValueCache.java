package com.bicycle.core.indicator.cache.value;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;

public interface ValueCache {
    
    public static final ValueCache NaN = new ValueCache() {
        @Override public void clear() {}
        @Override public float get(Symbol symbol, Timeframe timeframe) { return Float.NaN; }
        @Override public void set(Symbol symbol, Timeframe timeframe, float value) {}
    };
    
    void clear();

    float get(Symbol symbol, Timeframe timeframe);
    
    void set(Symbol symbol, Timeframe timeframe, float value);
    
}
