package com.bicycle.backtest.workflow.job.optimization;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluator;
import com.bicycle.backtest.strategy.trading.evaluator.robustness.RobustnessEvaluator;
import com.bicycle.backtest.strategy.trading.executor.TradingStrategyExecutor;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.symbol.Exchange;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptimizationContext {
    
    private List<MockPosition> positions;
    
    private final float initialMargin;
    private final Exchange exchange;
    private final IndicatorCache indicatorCache;
    private final int maxSymbolSelectionCount;
    private final TradingStrategyDefinition definition;
    private final RobustnessEvaluator robustnessEvaluator;
    private final PerformanceEvaluator performanceEvaluator;
    private final TradingStrategyExecutor tradingStrategyExecutor;
    
    public void clear() {
        indicatorCache.clear();
        positions = null;
    }
    
}
