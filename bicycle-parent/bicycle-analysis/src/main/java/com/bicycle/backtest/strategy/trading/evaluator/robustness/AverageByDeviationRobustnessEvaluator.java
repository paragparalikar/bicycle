package com.bicycle.backtest.strategy.trading.evaluator.robustness;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluator;
import java.util.Collection;
import java.util.stream.Collectors;

public class AverageByDeviationRobustnessEvaluator implements RobustnessEvaluator {

    @Override
    public double evaluate(Collection<Report> reports, PerformanceEvaluator evaluator) {
        final double averageCAGR = reports.stream().collect(Collectors.averagingDouble(Report::getCAGR));
        if(0 >= averageCAGR) return 0;
        final double average = reports.stream().collect(Collectors.averagingDouble(evaluator::evaluate));
        final double variance = reports.stream()
                .map(evaluator::evaluate)
                .collect(Collectors.averagingDouble(value -> Math.pow(average - value, 2)));
        final double standardDeviation = Math.sqrt(variance);
        final double robustness = average / standardDeviation;
        return Double.isNaN(robustness) ? 0 : robustness;
    }
    
}
