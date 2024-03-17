package com.bicycle.core.indicator.cache.series;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class SymbolTimeframeSeriesCache implements SeriesCache {
    
    private final Int2ObjectFunction<SeriesCache> function;
    private final Int2ObjectOpenHashMap<SeriesCache> cache;
    
    public SymbolTimeframeSeriesCache(int barCount, int symbolCount, int timeframeCount) {
        this.function = token -> new SymbolSeriesCache(barCount, symbolCount);
        this.cache = new Int2ObjectOpenHashMap<>(timeframeCount);
    }

    @Override
    public FloatSeries get(Symbol symbol, Timeframe timeframe) {
        return cache.computeIfAbsent(timeframe.ordinal(), function).get(symbol, timeframe);
    }

    @Override
    public void clear() {
        cache.values().forEach(SeriesCache::clear);
    }
}
