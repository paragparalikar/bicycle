package com.bicycle.backtest.workflow.job.optimization;

import com.bicycle.backtest.report.BaseReport;
import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.cache.SymbolTradingStrategyReportCache;
import com.bicycle.core.symbol.Symbol;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SymbolOptimizationJob implements OptimizationJob {
    
    private final OptimizationContext context;
    
    @Override
    public void optimize(ZonedDateTime startDate, ZonedDateTime endDate) {
        final SymbolTradingStrategyReportCache reportCache = new SymbolTradingStrategyReportCache(context.getInitialMargin(), 
                BaseReport.builder(context.getDefinition().getSymbols().size()), startDate, endDate);
        context.getTradingStrategyExecutor().execute(context.getDefinition(), startDate, endDate, reportCache);
        final Map<Symbol, Collection<Report>> reports = reportCache.findAll();
        final List<Symbol> symbols = reports.keySet().stream().distinct()
                .sorted(Collections.reverseOrder(Comparator.comparing(symbol -> 
            context.getRobustnessEvaluator().evaluate(reports.get(symbol), context.getPerformanceEvaluator()))))
            .limit(context.getMaxSymbolSelectionCount())
            .toList();
        
        context.getDefinition().getSymbols().clear();
        context.getDefinition().getSymbols().addAll(symbols);
    }
}
