package com.bicycle.backtest.strategy.trading.evaluator.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceEvaluatorType {
    
    CAGR("CAGR"),
    RaR("RaR"),
    AvgDD("AvgDD"),
    MaxDD("MaxDD"),
    RaRByMaxDD("RaR/MaxDD"), 
    RaRByAvgDD("RaR/AvgDD");
    
    private final String displayText;
    
    @Override
    public String toString() {
        return displayText;
    }

}
