package com.bicycle.backtest.strategy.trading.evaluator.performance;

import com.bicycle.backtest.report.Report;

public class AvgDDPerformanceEvaluator implements PerformanceEvaluator {

    @Override
    public double evaluate(Report report) {
        return 1 / report.getAvgDrawdown();
    }

}
