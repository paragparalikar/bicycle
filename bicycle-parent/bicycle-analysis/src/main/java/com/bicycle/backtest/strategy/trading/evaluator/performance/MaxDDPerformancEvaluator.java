package com.bicycle.backtest.strategy.trading.evaluator.performance;

import com.bicycle.backtest.report.Report;

public class MaxDDPerformancEvaluator implements PerformanceEvaluator {

    @Override
    public double evaluate(Report report) {
        return 1 / report.getMaxDrawdown();
    }

}
