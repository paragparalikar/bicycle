package com.bicycle.backtest.report.cache;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.symbol.Symbol;

public interface ReportCache {
    
    void clear();
    
    void compute(long date);
    
    Report get(Symbol symbol, MockTradingStrategy tradingStrategy);

}
