package com.bicycle.backtest.report.accumulator;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.ReportBuilder;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class PositionAccumulatorReport implements Report {
    
    public static ReportBuilder builder(ReportBuilder delegateBuilder) {
        return new PositionAccumulatorReportBuilder(delegateBuilder);
    }

    @Delegate private final Report delegate;
    @Getter private final List<MockPosition> positions = new ArrayList<>();
    
    public void open(MockPosition trade) {
        delegate.open(trade);
        positions.add(trade);
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        return delegate.unwrap(type);
    }
    
}

@RequiredArgsConstructor
class PositionAccumulatorReportBuilder implements ReportBuilder {
    private final ReportBuilder delegateBuilder;
    
    @Override
    public Report build(float initialMargin, MockTradingStrategy tradingStrategy, ZonedDateTime startDate, ZonedDateTime endDate) {
        return new PositionAccumulatorReport(delegateBuilder.build(initialMargin, tradingStrategy, startDate, endDate));
    }
    
}