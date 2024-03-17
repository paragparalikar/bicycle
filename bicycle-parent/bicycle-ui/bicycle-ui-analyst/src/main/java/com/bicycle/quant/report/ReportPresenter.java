package com.bicycle.quant.report;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.CallbackReport.Callback;
import com.bicycle.backtest.report.FullReport;
import com.bicycle.backtest.report.Report;
import com.bicycle.quant.Context;
import javafx.application.Platform;

public class ReportPresenter implements Callback {
    
    private long lastDisplayTimestamp, delay = 100;
    private ReportView view;
    
    public ReportPresenter(Context context) {
        context.addReportCallback(this);
        context.busyProperty().addListener((o, old, value) -> {
            if(value) clear();
        });
    }
    
    public void clear() {
        getView().clear();
    }
    
    public ReportView getView() {
        return null == view ? view = new ReportView() : view;
    }
    
    @Override
    public void onClose(MockPosition trade, Report report) {
        final long now = System.currentTimeMillis();
        if(lastDisplayTimestamp + delay < now) {
            lastDisplayTimestamp = now;
            final FullReport fullReport = report.unwrap(FullReport.class);
            if(null != fullReport) Platform.runLater(() -> getView().show(fullReport));
        }
    }

}
