package com.bicycle.backtest.report;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.CallbackReport.Callback;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import java.time.ZonedDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class CallbackReport implements Report {
    
    public static interface Callback {
        default void onCompute(long date, Report report) {}
        default void onOpen(MockPosition trade, Report report) {}
        default void onClose(MockPosition trade, Report report) {}
    }
    
    public static ReportBuilder builder(Callback callback, ReportBuilder delegateBuilder) {
        return new CallbackReportBuilder(callback, delegateBuilder);
    }
    
    @NonNull private final Callback callback;
    @NonNull @Delegate private final Report delegate;

    @Override
    public void compute(long date) {
        delegate.compute(date);
        callback.onCompute(date, delegate);
    }
    
    @Override
    public void open(MockPosition trade) {
        delegate.open(trade);
        callback.onOpen(trade, delegate);
    }
    
    @Override
    public void close(MockPosition trade) {
        delegate.close(trade);
        callback.onClose(trade, delegate);
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        return delegate.unwrap(type);
    }
    
}

@RequiredArgsConstructor
class CallbackReportBuilder implements ReportBuilder {
    
    private final Callback callback;
    private final ReportBuilder delegateBuilder;

    @Override
    public Report build(float initialMargin, MockTradingStrategy tradingStrategy, ZonedDateTime startDate, ZonedDateTime endDate) {
        return new CallbackReport(callback, delegateBuilder.build(initialMargin, tradingStrategy, startDate, endDate));
    }
    
}
