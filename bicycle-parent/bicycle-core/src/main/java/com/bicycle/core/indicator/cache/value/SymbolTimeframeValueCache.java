package com.bicycle.core.indicator.cache.value;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class SymbolTimeframeValueCache implements ValueCache {
    
    private final Int2ObjectFunction<ValueCache> function;
    private final Int2ObjectOpenHashMap<ValueCache> cache;
    
    public SymbolTimeframeValueCache(int symbolCount, int timeframeCount) {
        this.function = token -> new SymbolValueCache(symbolCount);
        this.cache = new Int2ObjectOpenHashMap<>(timeframeCount);
    }
    
    @Override
    public float get(Symbol symbol, Timeframe timeframe) {
         return cache.getOrDefault(timeframe.ordinal(), ValueCache.NaN).get(symbol, timeframe);
    }

    @Override
    public void set(Symbol symbol, Timeframe timeframe, float value) {
        cache.computeIfAbsent(timeframe.ordinal(), function).set(symbol, timeframe, value);
    }

    @Override
    public void clear() {
        cache.values().forEach(ValueCache::clear);
    }
    
}
