package com.bicycle.core.indicator.cache.series;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class SymbolSeriesCache implements SeriesCache {
    
    private final Int2ObjectFunction<FloatSeries> function;
    private final Int2ObjectOpenHashMap<FloatSeries> cache;
    
    public SymbolSeriesCache(int barCount, int symbolCount) {
        this.function = token -> new FloatSeries(barCount + 1);
        this.cache = new Int2ObjectOpenHashMap<>(symbolCount);
    }

    @Override
    public FloatSeries get(Symbol symbol, Timeframe timeframe) {
        return cache.computeIfAbsent(symbol.token(), function);
    }
    
    @Override
    public void clear() {
        cache.values().forEach(FloatSeries::clear);
    }

}
