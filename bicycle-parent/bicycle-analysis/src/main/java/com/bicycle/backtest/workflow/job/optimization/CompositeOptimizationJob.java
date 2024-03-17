package com.bicycle.backtest.workflow.job.optimization;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class CompositeOptimizationJob implements OptimizationJob {

    private final List<OptimizationJob> optimizationJobs;
    
    public CompositeOptimizationJob(OptimizationContext context) {
        this.optimizationJobs = Arrays.asList(
                new TimeframeOptimizationJob(context), 
                new SymbolOptimizationJob(context), 
                new ParameterOptimizationJob(context), 
                new EntryTimeOptimizationJob(context));
    }
    
    @Override
    public void optimize(ZonedDateTime startDate, ZonedDateTime endDate) {
        optimizationJobs.forEach(job -> job.optimize(startDate, endDate));
    }

}
