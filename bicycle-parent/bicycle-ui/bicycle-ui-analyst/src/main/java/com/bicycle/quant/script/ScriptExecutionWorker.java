package com.bicycle.quant.script;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.report.cache.TradingStrategyReportCache;
import com.bicycle.backtest.strategy.positionSizing.PercentageInitialMarginPositionSizingStrategy;
import com.bicycle.backtest.strategy.positionSizing.PositionSizingStrategy;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.backtest.strategy.trading.builder.ScriptTradingStrategyBuilder;
import com.bicycle.backtest.strategy.trading.builder.TradingStrategyBuilder;
import com.bicycle.client.yahoo.adapter.YahooSymbolDataProvider;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.dataSource.BarDataSource;
import com.bicycle.core.bar.dataSource.FileSystemBarDataSource;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.repository.CacheSymbolRepository;
import com.bicycle.core.symbol.repository.SymbolRepository;
import com.bicycle.quant.Context;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import javafx.application.Platform;
import javafx.concurrent.Task;

@Builder
@RequiredArgsConstructor
public class ScriptExecutionWorker extends Task<Void> {

    private final String text;
    private final Context context;
    
    @Override
    protected Void call() throws Exception {
        try {
            final ZonedDateTime toDate = context.settings().getToDate();
            final ZonedDateTime fromDate = context.settings().getFromDate();
            final Exchange exchange = context.settings().getExchange();
            final Timeframe timeframe = context.settings().getTimeframe();
            final float initialMargin = (float) context.settings().getInitialMargin();
            final float slippagePercentage = (float) context.settings().getSlippagePercentage();
            final PositionSizingStrategy positionSizingStrategy = 
                    new PercentageInitialMarginPositionSizingStrategy(
                            (float) context.settings().getPositionSizePercentage(),
                            context.settings().isLimitToAvailableMargin());
            
            if(Timeframe.D.equals(timeframe)) {
                final SymbolRepository symbolRepository = new CacheSymbolRepository(new YahooSymbolDataProvider());
                final Collection<Symbol> allSymbols = symbolRepository.findByExchange(exchange);
                final IndicatorCache indicatorCache = new IndicatorCache(allSymbols.size(), 1);
                final ReportCache reportCache = new TradingStrategyReportCache(initialMargin, 
                        context.createReportBuilder(allSymbols.size()), fromDate, toDate);
                final TradingStrategyBuilder tradingStrategyBuilder = new ScriptTradingStrategyBuilder(text, indicatorCache);
                final MockTradingStrategy tradingStrategy = tradingStrategyBuilder.buildDefault(
                        slippagePercentage, indicatorCache, reportCache, positionSizingStrategy);
                final BarDataSource barDataSource = new FileSystemBarDataSource(symbolRepository);
                
                try(BarReader reader = barDataSource.get(exchange, timeframe)){
                    long lastBarDate = 0;
                    final Bar bar = new Bar();
                    for(int index = 0; index < reader.size(); index++) {
                        reader.readInto(bar);
                        indicatorCache.onBar(bar);
                        tradingStrategy.onBar(bar);
                        if(lastBarDate != bar.date()) reportCache.compute(lastBarDate = bar.date());
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(() -> context.busyProperty().set(false));
        }
        return null;
    }

}
