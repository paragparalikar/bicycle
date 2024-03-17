package com.bicycle.backtest.strategy.trading.builder;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.positionSizing.PositionSizingStrategy;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.indicator.IndicatorCache;
import java.util.List;

public interface TradingStrategyBuilder {
    
    List<MockTradingStrategy> build(float slippagePercentage, IndicatorCache indicatorCache, 
            ReportCache reportCache, PositionSizingStrategy positionSizingStrategy);
    
    MockTradingStrategy buildDefault(float slippagePercentage, IndicatorCache indicatorCache, 
            ReportCache reportCache, PositionSizingStrategy positionSizingStrategy);
    
}
