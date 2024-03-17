package com.bicycle.backtest.report.cache;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.ReportBuilder;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.symbol.Symbol;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TradingStrategyReportCache implements ReportCache {
    
    private final float initialMargin;
    private final ReportBuilder reportBuilder;
    private final ZonedDateTime startDate, endDate;
    private final ConcurrentHashMap<String, Report> cache = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        cache.values().forEach(report -> report.clear());
    }
    
    public Collection<Report> findAll(){
        return cache.values();
    }

    @Override
    public Report get(Symbol symbol, MockTradingStrategy tradingStrategy) {
        return cache.computeIfAbsent(tradingStrategy.toString(),
                text -> reportBuilder.build(initialMargin, tradingStrategy, startDate, endDate));
    }
    
    @Override
    public void compute(long date) {
        for(Report report : cache.values()) report.compute(date);
        System.out.println(new Date(date));
    }
    
}
