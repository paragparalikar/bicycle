package com.bicycle.backtest.strategy.trading.builder;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.positionSizing.PositionSizingStrategy;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.rule.LiquidityRule;
import com.bicycle.core.rule.Rule;
import java.util.Collections;
import java.util.List;

public class SingleTradingStrategyBuilder implements TradingStrategyBuilder {
    
    @Override
    public List<MockTradingStrategy> build(float slippagePercentage, 
            IndicatorCache cache, ReportCache reportCache, PositionSizingStrategy positionSizingStrategy) {
        return Collections.singletonList(buildDefault(slippagePercentage, cache, reportCache, positionSizingStrategy));
    }

    @Override
    public MockTradingStrategy buildDefault(float slippagePercentage, IndicatorCache cache, 
            ReportCache reportCache, PositionSizingStrategy positionSizingStrategy) {
        final Rule entryRule = new LiquidityRule(cache)
                .and(cache.close().greaterThan(cache.sma(cache.close(), 100)))
                .and(cache.rsi(cache.close(), 2).lesserThan(10));
        final Rule exitRule = cache.close().greaterThan(cache.sma(cache.close(), 5));
        return new MockTradingStrategy(slippagePercentage,
                entryRule, exitRule, OrderType.BUY, reportCache, cache.atr(14), positionSizingStrategy);
    }

}
