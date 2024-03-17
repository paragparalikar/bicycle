package com.bicycle.core.indicator.cache.series;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.FloatSeries;

public interface SeriesCache {
    
    void clear();
    
    FloatSeries get(Symbol symbol, Timeframe timeframe);

}
