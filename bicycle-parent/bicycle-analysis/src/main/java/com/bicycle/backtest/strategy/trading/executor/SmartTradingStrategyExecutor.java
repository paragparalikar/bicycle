package com.bicycle.backtest.strategy.trading.executor;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import com.bicycle.core.bar.dataSource.BarDataSource;
import com.bicycle.core.indicator.IndicatorCache;
import java.time.ZonedDateTime;
import lombok.Builder;

public class SmartTradingStrategyExecutor implements TradingStrategyExecutor {
    
    private final IndicatorCache indicatorCache;
    private final BarDataSource barDataSource;
    private TradingStrategyExecutor serialTradingStrategyExecutor;
    private TradingStrategyExecutor parallelTradingStrategyExecutor;
    
    private TradingStrategyExecutor getSerialTradingStrategyExecutor() {
        return null == serialTradingStrategyExecutor ? 
                serialTradingStrategyExecutor = new SerialTradingStrategyExecutor(barDataSource, indicatorCache)
                : serialTradingStrategyExecutor;
    }
    
    private TradingStrategyExecutor getParallelTradingStrategyExecutor() {
        return null == parallelTradingStrategyExecutor ? 
                parallelTradingStrategyExecutor = new ParallelTradingStrategyExecutor(barDataSource, indicatorCache)
                : parallelTradingStrategyExecutor;
    } 
    
    @Builder
    public SmartTradingStrategyExecutor(BarDataSource barDataSource, IndicatorCache indicatorCache) {
        this.indicatorCache = indicatorCache;
        this.barDataSource = barDataSource;
    }
    
    @Override
    public void execute(TradingStrategyDefinition definition, ZonedDateTime startDate, 
            ZonedDateTime endDate, ReportCache reportCache) {
        final TradingStrategyExecutor tradingStrategyExecutor = 
                Runtime.getRuntime().availableProcessors() * 10 < definition.getTradingStrategies().size() ?
                getParallelTradingStrategyExecutor() : getSerialTradingStrategyExecutor();
        System.out.println("Using " + tradingStrategyExecutor.getClass().getSimpleName() + " for execution");
        tradingStrategyExecutor.execute(definition, startDate, endDate, reportCache);
    }
    
}
