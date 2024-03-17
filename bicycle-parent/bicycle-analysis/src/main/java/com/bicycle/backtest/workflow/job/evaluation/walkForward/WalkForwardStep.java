package com.bicycle.backtest.workflow.job.evaluation.walkForward;

import java.time.ZonedDateTime;

public interface WalkForwardStep {

    void execute(ZonedDateTime startDate, ZonedDateTime endDate);
    
}
