package com.bicycle.backtest.strategy.trading.evaluator.robustness;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluator;
import java.util.Collection;
import java.util.stream.Collectors;

public class AverageRobustnessEvaluator implements RobustnessEvaluator {

    @Override
    public double evaluate(Collection<Report> reports, PerformanceEvaluator evaluator) {
        return reports.stream().collect(Collectors.averagingDouble(evaluator::evaluate));
    }

}
