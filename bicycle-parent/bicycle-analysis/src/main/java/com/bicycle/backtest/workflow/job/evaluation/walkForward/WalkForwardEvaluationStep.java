package com.bicycle.backtest.workflow.job.evaluation.walkForward;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.report.cache.SingletonReportCache;
import com.bicycle.backtest.strategy.trading.TradingStrategyDefinition;
import com.bicycle.backtest.workflow.job.evaluation.EvaluationContext;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WalkForwardEvaluationStep implements WalkForwardStep {

    private final EvaluationContext evaluationContext;

    @Override
    public void execute(ZonedDateTime startDate, ZonedDateTime endDate) {
        final TradingStrategyDefinition tradingStrategyDefinition = evaluationContext.getTradingStrategyDefinition();
        final ReportCache reportCache = new SingletonReportCache(evaluationContext.getReport());
        evaluationContext.getTradingStrategyExecutor().execute(
                tradingStrategyDefinition,
                evaluationContext.getStartDate(), 
                evaluationContext.getEndDate(), 
                reportCache);
    }

}