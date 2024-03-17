package com.bicycle.core.indicator.cache.value;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

public class SymbolValueCache implements ValueCache {
    
    private final Int2FloatOpenHashMap cache;
    
    public SymbolValueCache(int symbolCount) {
        this.cache = new Int2FloatOpenHashMap(symbolCount);
    }

    @Override
    public float get(Symbol symbol, Timeframe timeframe) {
        return cache.getOrDefault(symbol.token(), Float.NaN);
    }

    @Override
    public void set(Symbol symbol, Timeframe timeframe, float value) {
        cache.put(symbol.token(), value);
    }
    
    @Override
    public void clear() {
        cache.clear();
    }

}
