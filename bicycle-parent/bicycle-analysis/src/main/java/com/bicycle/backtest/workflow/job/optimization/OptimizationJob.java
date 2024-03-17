package com.bicycle.backtest.workflow.job.optimization;

import java.time.ZonedDateTime;

public interface OptimizationJob {

    void optimize(ZonedDateTime startDate, ZonedDateTime endDate);
    
}
