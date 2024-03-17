package com.bicycle.backtest.strategy.trading.evaluator.performance;

public class PerformanceEvaluatorFactory {
    
    public PerformanceEvaluator get(PerformanceEvaluatorType type) {
        switch(type) {
            case AvgDD:
                return new AvgDDPerformanceEvaluator();
            case CAGR:
                return new CAGRPerformanceEvaluator();
            case MaxDD:
                return new MaxDDPerformancEvaluator();
            case RaR:
                return new RaRPerformanceEvaluator();
            case RaRByAvgDD:
                return new RarByAvgDDPerformanceEvaluator();
            case RaRByMaxDD:
                return new RaRByMaxDDPerformanceEvaluator();
            default:
                throw new IllegalArgumentException(String.format(
                        "PerformanceEvaluatorType %s is not supported", type.name()));
        }
    }

}
