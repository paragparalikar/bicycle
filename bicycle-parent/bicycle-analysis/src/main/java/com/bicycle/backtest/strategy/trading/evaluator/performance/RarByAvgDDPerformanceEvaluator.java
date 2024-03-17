package com.bicycle.backtest.strategy.trading.evaluator.performance;

import com.bicycle.backtest.report.Report;

public class RarByAvgDDPerformanceEvaluator implements PerformanceEvaluator {

    @Override
    public double evaluate(Report report) {
        return report.getCAGR() / (report.getExposure() * report.getAvgDrawdown());
    }

}
