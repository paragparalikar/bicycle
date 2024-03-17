package com.bicycle.backtest.strategy.trading.evaluator.performance;

import com.bicycle.backtest.report.Report;

public class CAGRPerformanceEvaluator implements PerformanceEvaluator {

    @Override
    public double evaluate(Report report) {
        return report.getCAGR();
    }

}
