package com.bicycle.backtest.workflow.job.evaluation;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import com.bicycle.backtest.strategy.trading.executor.TradingStrategyExecutor;
import com.bicycle.backtest.workflow.job.evaluation.monteCarlo.MonteCarloReport;
import com.bicycle.core.indicator.IndicatorCache;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class EvaluationContext {
    
    private Report report;
    private MonteCarloReport monteCarloReport;
    private TradingStrategyDefinition tradingStrategyDefinition;
    
    private final float initialMargin;
    private final IndicatorCache indicatorCache;
    private final ZonedDateTime startDate, endDate;
    private final TradingStrategyExecutor tradingStrategyExecutor;

}
