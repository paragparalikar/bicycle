package com.bicycle.backtest.strategy.trading.evaluator.robustness;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluator;
import java.util.Collection;
import java.util.stream.Collectors;

public class DeviationRobustnessEvaluator implements RobustnessEvaluator {

    @Override
    public double evaluate(Collection<Report> reports, PerformanceEvaluator evaluator) {
        final double average = reports.stream().collect(Collectors.averagingDouble(evaluator::evaluate));
        return reports.stream()
                .map(evaluator::evaluate)
                .collect(Collectors.averagingDouble(value -> Math.pow(average - value, 2)));
    }

}
