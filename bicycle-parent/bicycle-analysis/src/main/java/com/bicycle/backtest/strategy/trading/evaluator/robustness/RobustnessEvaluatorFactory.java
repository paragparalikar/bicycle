package com.bicycle.backtest.strategy.trading.evaluator.robustness;

public class RobustnessEvaluatorFactory {
    
    public RobustnessEvaluator get(RobustnessEvaluatorType type) {
        switch(type) {
            case AVERAGE:
                return new AverageRobustnessEvaluator();
            case AVERAGE_BY_DEVIATION:
                return new AverageByDeviationRobustnessEvaluator();
            case DEVIATION:
                return new DeviationRobustnessEvaluator();
            case MAXIMUM:
                return new MaximumRobustnessEvaluator();
            default:
                throw new IllegalArgumentException(String.format("RobustnessEvaluatorType %s is not supported", type.name()));
        }
    }

}
