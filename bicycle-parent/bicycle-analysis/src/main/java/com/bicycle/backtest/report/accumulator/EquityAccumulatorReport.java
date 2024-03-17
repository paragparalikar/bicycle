package com.bicycle.backtest.report.accumulator;

import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.ReportBuilder;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class EquityAccumulatorReport implements Report {
    
    public static ReportBuilder builder(ReportBuilder delegateBuilder) {
        return new EquityAccumulatorReportBuilder(delegateBuilder);
    }
    
    @Delegate private final Report delegate;
    @Getter private final Map<Long, Float> equities = new HashMap<>();
    
    public void compute(long date) {
        delegate.compute(date);
        equities.put(date, delegate.getEquity());
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        return delegate.unwrap(type);
    }

}

@RequiredArgsConstructor
class EquityAccumulatorReportBuilder implements ReportBuilder {
    private final ReportBuilder delegateBuilder;
    
    @Override
    public Report build(float initialMargin, MockTradingStrategy tradingStrategy, ZonedDateTime startDate, ZonedDateTime endDate) {
        return new EquityAccumulatorReport(delegateBuilder.build(initialMargin, tradingStrategy, startDate, endDate));
    }
    
}