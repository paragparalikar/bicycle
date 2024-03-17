package com.bicycle.backtest.workflow.job.optimization;

import com.bicycle.backtest.MockPosition;
import com.bicycle.core.rule.DayOfWeekRule;
import com.bicycle.core.rule.MonthOfYearRule;
import com.bicycle.core.rule.WeekOfMonthRule;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryTimeOptimizationJob implements OptimizationJob {
    
    private final OptimizationContext context;
    
    @Override
    public void optimize(ZonedDateTime startDate, ZonedDateTime endDate) {
        optimizeMonth();
        optimizeDayOfWeek();
        optimizeWeekOfMonth();
    }
    
    private <T> Collection<T> optimize(Function<MockPosition, T> function){
        final Map<T, Float> scores = context.getPositions().stream()
                .collect(Collectors.toMap(function, position -> position.getClosePercentProfitLoss(), (one, two) -> one + two));
        final double threshold = scores.values().stream()
                .filter(value -> 0 < value)
                .collect(Collectors.averagingDouble(Float::doubleValue)) / 2;
        final Collection<T> results = scores.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Entry::getKey)
                .distinct()
                .toList();
        return scores.size() == results.size() ? Collections.emptyList() : results;
    }
    
    private void optimizeMonth() {
        final Collection<Month> months = optimize(position -> Month.from(Instant.ofEpochMilli(position.getEntryDate())));
        if(!months.isEmpty()) context.getDefinition().getTradingStrategies()
            .forEach(strategy -> strategy.addEntryRule(new MonthOfYearRule(context.getIndicatorCache(), months)));
    }
    
    private void optimizeWeekOfMonth() {
        final Collection<Integer> weeksOfMonth = optimize(position -> Instant.ofEpochMilli(position.getEntryDate()).get(ChronoField.DAY_OF_MONTH) / 7);
        if(!weeksOfMonth.isEmpty()) context.getDefinition().getTradingStrategies()
            .forEach(strategy -> strategy.addEntryRule(new WeekOfMonthRule(context.getIndicatorCache(), weeksOfMonth)));
    }
    
    private void optimizeDayOfWeek() {
        final Collection<DayOfWeek> daysOfWeek = optimize(position -> DayOfWeek.from(Instant.ofEpochMilli(position.getEntryDate())));
        if(!daysOfWeek.isEmpty()) context.getDefinition().getTradingStrategies()
            .forEach(strategy -> strategy.addEntryRule(new DayOfWeekRule(context.getIndicatorCache(), daysOfWeek)));
    }
    
}
