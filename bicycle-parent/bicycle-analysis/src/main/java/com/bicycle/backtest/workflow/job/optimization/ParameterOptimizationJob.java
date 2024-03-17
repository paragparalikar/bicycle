package com.bicycle.backtest.workflow.job.optimization;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.BaseReport;
import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.accumulator.PositionAccumulatorReport;
import com.bicycle.backtest.report.cache.TradingStrategyReportCache;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParameterOptimizationJob implements OptimizationJob {
    
    private final OptimizationContext context;
    
    @Override
    public void optimize(ZonedDateTime startDate, ZonedDateTime endDate) {
        final TradingStrategyReportCache reportCache = new TradingStrategyReportCache(context.getInitialMargin(), 
                PositionAccumulatorReport.builder(BaseReport.builder(context.getDefinition().getSymbols().size())), startDate, endDate);
        context.getTradingStrategyExecutor().execute(context.getDefinition(), startDate, endDate, reportCache);
        final List<Report> reports = new ArrayList<>(reportCache.findAll());
        
        final List<MockPosition> positions = reports.stream()
                .map(PositionAccumulatorReport.class::cast)
                .map(PositionAccumulatorReport::getPositions)
                .flatMap(Collection::stream)
                .toList();
        context.setPositions(positions);
        
        final List<Double> performanceScores = reports.stream() 
                .map(context.getPerformanceEvaluator()::evaluate)
                .toList();
        final List<MockTradingStrategy> tradingStrategies = reports.stream()
                .sorted(Comparator.comparing(report -> score(report, reports, performanceScores)))
                .map(Report::getTradingStrategy)
                .limit(10).toList();
        
        context.getDefinition().getTradingStrategies().clear();
        context.getDefinition().getTradingStrategies().addAll(tradingStrategies);
    }
    
    private double score(Report report, List<Report> reports, List<Double> performanceScores) {
        double sum = 0;
        for(int index = 0; index < reports.size(); index++) {
            final Report other = reports.get(index);
            if(report == other) continue;
            final double performanceScore = performanceScores.get(index);
            final double distance = report.getTradingStrategy().distance(other.getTradingStrategy());
            sum += performanceScore / Math.exp(distance);
        }
        return sum;
    }

}
