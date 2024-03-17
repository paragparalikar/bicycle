package com.bicycle.backtest.strategy.trading.evaluator.performance;

import com.bicycle.backtest.report.Report;

public interface PerformanceEvaluator {

    double evaluate(Report report);
    
}
