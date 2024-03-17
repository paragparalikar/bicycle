package com.bicycle.backtest.strategy.trading.executor;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import java.time.ZonedDateTime;

public interface TradingStrategyExecutor {

    void execute(
            TradingStrategyDefinition definition, 
            ZonedDateTime startDate, 
            ZonedDateTime endDate, 
            ReportCache reportCache);
    
}
