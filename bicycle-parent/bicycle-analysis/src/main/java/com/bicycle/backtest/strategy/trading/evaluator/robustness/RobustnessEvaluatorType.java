package com.bicycle.backtest.strategy.trading.evaluator.robustness;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RobustnessEvaluatorType {
    
    MAXIMUM("Maximum"),
    AVERAGE("Average"),
    DEVIATION("Deviation"),
    AVERAGE_BY_DEVIATION("Average/Deviation");
    
    private final String displayText;
    
    @Override
    public String toString() {
        return displayText;
    }

}
