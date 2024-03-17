package com.bicycle.backtest.report;

import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import java.time.ZonedDateTime;

public interface ReportBuilder {
    
    Report build(float initialMargin, MockTradingStrategy tradingStrategy, ZonedDateTime startDate, ZonedDateTime endDate);

}
