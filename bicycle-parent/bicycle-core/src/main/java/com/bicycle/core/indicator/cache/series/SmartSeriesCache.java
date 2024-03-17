package com.bicycle.core.indicator.cache.series;

import lombok.experimental.Delegate;

public class SmartSeriesCache implements SeriesCache {
    
    @Delegate private final SeriesCache delegate;
    
    public SmartSeriesCache(int barCount, int symbolCount, int timeframeCount) {
        this.delegate = 1 < timeframeCount ? 
                new SymbolTimeframeSeriesCache(barCount, symbolCount, timeframeCount) :
                new SymbolSeriesCache(barCount, symbolCount);
    }
    
}
