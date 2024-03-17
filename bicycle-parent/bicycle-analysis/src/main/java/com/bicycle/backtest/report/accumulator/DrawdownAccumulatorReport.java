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
public class DrawdownAccumulatorReport implements Report {
    
    public static ReportBuilder builder(ReportBuilder delegateBuilder) {
        return new DrawdownAccumulatorReportBuilder(delegateBuilder);
    }
    
    @Delegate private final Report delegate;
    @Getter private final Map<Long, Float> drawdowns = new HashMap<>();
    
    public void compute(long date) {
        delegate.compute(date);
        drawdowns.put(date, delegate.getDrawdown());
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        return delegate.unwrap(type);
    }

}


@RequiredArgsConstructor
class DrawdownAccumulatorReportBuilder implements ReportBuilder {
    private final ReportBuilder delegateBuilder;
    
    @Override
    public Report build(float initialMargin, MockTradingStrategy tradingStrategy, ZonedDateTime startDate, ZonedDateTime endDate) {
        return new DrawdownAccumulatorReport(delegateBuilder.build(initialMargin, tradingStrategy, startDate, endDate));
    }
    
}