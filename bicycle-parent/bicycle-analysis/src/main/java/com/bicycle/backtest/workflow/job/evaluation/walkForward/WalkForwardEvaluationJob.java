package com.bicycle.backtest.workflow.job.evaluation.walkForward;

import com.bicycle.backtest.workflow.job.evaluation.EvaluationContext;
import com.bicycle.backtest.workflow.job.evaluation.EvaluationJob;
import com.bicycle.backtest.workflow.job.optimization.OptimizationContext;
import java.time.Duration;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WalkForwardEvaluationJob implements EvaluationJob {

    private final Duration trainDuration, testDuration;
    private final OptimizationContext optimizationContext;

    @Override
    public void evaluate(EvaluationContext evaluationContext) {
        
        final WalkForwardOptimizationStep optimizationStep = new WalkForwardOptimizationStep(evaluationContext, optimizationContext);
        final WalkForwardEvaluationStep evaluationStep = new WalkForwardEvaluationStep(evaluationContext);
        ZonedDateTime trainStartDate = evaluationContext.getStartDate();
        ZonedDateTime trainEndDate = trainStartDate.plus(trainDuration);
        ZonedDateTime testEndDate = trainEndDate.plus(testDuration);
        while (!testEndDate.isAfter(evaluationContext.getEndDate())) {
            optimizationStep.execute(trainStartDate, trainEndDate);
            evaluationStep.execute(trainEndDate, testEndDate);
            trainStartDate = testEndDate;
            trainEndDate = trainStartDate.plus(trainDuration);
            testEndDate = trainEndDate.plus(testDuration);
        }
    }

}
