package com.bicycle.core.indicator.cache.value;

import lombok.experimental.Delegate;

public class SmartValueCache implements ValueCache {

    @Delegate private final ValueCache delegate;
    
    public SmartValueCache(int symbolCount, int timeframeCount) {
        this.delegate = 1 < timeframeCount ? 
                new SymbolTimeframeValueCache(symbolCount, timeframeCount) :
                new SymbolValueCache(symbolCount);
    }

}
