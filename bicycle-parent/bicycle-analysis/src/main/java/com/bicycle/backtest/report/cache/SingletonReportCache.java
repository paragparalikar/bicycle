package com.bicycle.backtest.report.cache;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingletonReportCache implements ReportCache {
    
    private final Report report;

    @Override
    public void compute(long date) {
        report.compute(date);
    }
    
    @Override
    public void clear() {
        report.clear();
    }
    
    public Report get() {
        return report;
    }

    @Override
    public Report get(Symbol symbol, MockTradingStrategy tradingStrategy) {
        return report;
    }

}
