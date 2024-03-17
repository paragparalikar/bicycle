package com.bicycle.core.bar.repository;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;
import java.util.List;

public interface BarRepository extends AutoCloseable {

    void init();
    
    void persist(Collection<Bar> bars);
    
    long getLastDownloadDate(Symbol symbol, Timeframe timeframe);
    
    void persist(Symbol symbol, Timeframe timeframe, List<Bar> bars);
    
    List<Bar> findBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe);
    
    BarReader readBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe);
    
    List<Bar> findBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe, int limit);

    List<Bar> findBySymbolAndTimeframeAfter(Symbol symbol, Timeframe timeframe, long startExclusive);

    
    
}
