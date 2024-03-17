package com.bicycle.backtest.workflow.job.evaluation.monteCarlo;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.accumulator.PositionAccumulatorReport;
import com.bicycle.backtest.workflow.job.evaluation.EvaluationContext;
import com.bicycle.backtest.workflow.job.evaluation.EvaluationJob;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MonteCarloEvaluationJob implements EvaluationJob {
    
    private final int iterationCount;
    private final double sampleRatio;

    @Override
    public void evaluate(EvaluationContext evaluationContext) {
        if(evaluationContext.getReport() instanceof PositionAccumulatorReport report) {
            
            final List<MockPosition> positions = report.getPositions();
            final float equities[] = new float[iterationCount], maxDrawdowns[]  = new float[iterationCount];
            
            IntStream.range(0, iterationCount).parallel().forEach(iterationIndex -> {
                float equity = 0, maxEquity = 0, drawdown = 0, maxDrawdown = Float.MIN_VALUE;
                for(int index = 0; index < positions.size(); index++) {
                    final MockPosition position = positions.get(index);
                    if(!position.isOpen() && Math.random() <= sampleRatio) {
                        equity += position.getClosePercentProfitLoss();
                        maxEquity = Math.max(equity, maxEquity);
                        drawdown = maxEquity - equity;
                        maxDrawdown = Math.max(drawdown, maxDrawdown);
                    }
                }
                equities[iterationIndex] = equity;
                maxDrawdowns[iterationIndex] = maxDrawdown;
            });
            
            final MonteCarloReport monteCarloReport = new MonteCarloReport(equities, maxDrawdowns);
            evaluationContext.setMonteCarloReport(monteCarloReport);
            
        }
    }
    
}


