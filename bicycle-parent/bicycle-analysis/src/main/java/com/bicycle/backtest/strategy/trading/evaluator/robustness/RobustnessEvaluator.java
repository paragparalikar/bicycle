package com.bicycle.backtest.strategy.trading.evaluator.robustness;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluator;
import java.util.Collection;

public interface RobustnessEvaluator {

    double evaluate(Collection<Report> reports, PerformanceEvaluator evaluator);

}
