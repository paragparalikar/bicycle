package com.bicycle.backtest.strategy.trading.executor;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.dataSource.BarDataSource;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.symbol.Symbol;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

@Builder
@RequiredArgsConstructor
public class ParallelTradingStrategyExecutor implements TradingStrategyExecutor {

    private final BarDataSource barDataSource;
    private final IndicatorCache indicatorCache;
    
    @Override
    @SneakyThrows
    public void execute(TradingStrategyDefinition definition, ZonedDateTime startDate, 
            ZonedDateTime endDate, ReportCache reportCache) {
        reportCache.clear();
        indicatorCache.clear();
        final Bar bar = new Bar();
        final List<TradingStrategyRunner> runners = createRunners(bar, definition.getTradingStrategies());
        final IntList symbolCache = new IntArrayList(definition.getSymbols().stream().map(Symbol::token).toList());
        for(Timeframe timeframe : definition.getTimeframes()) {
            try(BarReader reader = barDataSource.get(definition.getExchange(), timeframe, startDate, endDate)){
                long lastBarDate = 0;
                for(int index = 0; index < reader.size(); index++) {
                    reader.readInto(bar);
                    if(symbolCache.contains(bar.symbol().token())) {
                        indicatorCache.onBar(bar);
                        runners.parallelStream().forEach(TradingStrategyRunner::run);
                        if(lastBarDate != bar.date()) reportCache.compute(lastBarDate = bar.date());
                    }
                }
            }
        }
    }
    
    private List<TradingStrategyRunner> createRunners(Bar bar, Collection<MockTradingStrategy> tradingStrategies){
        final AtomicInteger counter = new AtomicInteger(0);
        final int processorCount = Runtime.getRuntime().availableProcessors();
        return tradingStrategies.stream()
            .collect(Collectors.groupingBy(strategy -> counter.incrementAndGet() / processorCount))
            .values().stream()
            .map(strategies -> new TradingStrategyRunner(bar, strategies))
            .toList();
    }

}

@RequiredArgsConstructor
class TradingStrategyRunner implements Runnable {
    
    private final Bar bar;
    private final List<MockTradingStrategy> tradingStrategies;
    
    @Override
    public void run() {
        for(int index = 0; index < tradingStrategies.size(); index++) {
            tradingStrategies.get(index).onBar(bar);
        }
    }
    
}
