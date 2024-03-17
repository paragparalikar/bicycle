package com.bicycle.backtest.strategy.trading;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradingStrategyDefinition implements Cloneable {

    private final Exchange exchange;
    private final Collection<Symbol> symbols = new HashSet<>();
    private final Collection<Timeframe> timeframes = new HashSet<>();
    private final Collection<MockTradingStrategy> tradingStrategies = new ArrayList<>();
    
    @Override
    protected TradingStrategyDefinition clone() {
        final TradingStrategyDefinition clone = new TradingStrategyDefinition(exchange);
        clone.getSymbols().addAll(symbols);
        clone.getTimeframes().addAll(timeframes);
        clone.getTradingStrategies().addAll(tradingStrategies);
        return clone;
    }
    
}
