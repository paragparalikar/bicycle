package com.bicycle.backtest.workflow.job.evaluation.walkForward;

import com.bicycle.backtest.workflow.job.evaluation.EvaluationContext;
import com.bicycle.backtest.workflow.job.optimization.CompositeOptimizationJob;
import com.bicycle.backtest.workflow.job.optimization.OptimizationContext;
import com.bicycle.backtest.workflow.job.optimization.OptimizationJob;
import java.time.ZonedDateTime;

public class WalkForwardOptimizationStep implements WalkForwardStep {

    private final OptimizationJob optimizationJob;
    private final EvaluationContext evaluationContext;
    private final OptimizationContext optimizationContext;
    
    public WalkForwardOptimizationStep(
            EvaluationContext evaluationContext, 
            OptimizationContext optimizationContext) {
        this.evaluationContext = evaluationContext;
        this.optimizationContext = optimizationContext;
        this.optimizationJob = new CompositeOptimizationJob(optimizationContext);
    }
    
    @Override
    public void execute(ZonedDateTime startDate, ZonedDateTime endDate) {
        optimizationContext.clear();
        optimizationJob.optimize(startDate, endDate);
        evaluationContext.setTradingStrategyDefinition(optimizationContext.getDefinition());
    }

}
