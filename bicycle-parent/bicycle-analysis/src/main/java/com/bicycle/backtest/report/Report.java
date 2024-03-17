package com.bicycle.backtest.report;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.symbol.Symbol;

public interface Report {
    
    void clear();
    
    void compute(long date);
    
    void open(MockPosition trade);
    
    void close(MockPosition trade);
    
    MockTradingStrategy getTradingStrategy();
    
    MockPosition getOpenPosition(Symbol symbol);
    
    
    float getCAGR();
    
    float getEquity();
    
    float getExposure();
    
    float getDrawdown();
    
    float getAvgDrawdown();
    
    float getMaxDrawdown();
    
    float getInitialMargin();
    
    float getAvailableMargin();
    
    <T extends Report> T unwrap(Class<T> type);

}
