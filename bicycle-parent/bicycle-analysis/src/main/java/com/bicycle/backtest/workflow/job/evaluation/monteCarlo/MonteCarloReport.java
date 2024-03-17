package com.bicycle.backtest.workflow.job.evaluation.monteCarlo;

import lombok.Value;

@Value
public class MonteCarloReport {

    private final float[] equity;
    private final float[] maxDrawdown;
    
}
