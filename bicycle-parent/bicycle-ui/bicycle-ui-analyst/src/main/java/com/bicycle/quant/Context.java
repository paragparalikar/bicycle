package com.bicycle.quant;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.CallbackReport;
import com.bicycle.backtest.report.CallbackReport.Callback;
import com.bicycle.backtest.report.FullReport;
import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.ReportBuilder;
import com.bicycle.quant.settings.Settings;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import atlantafx.base.controls.ModalPane;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

@Getter @Accessors(fluent = true) @RequiredArgsConstructor
public class Context implements Callback {
    
    private final Stage stage;
    private final ModalPane modalPane;

    private final Settings settings = new Settings();
    private final List<Callback> callbacks = new ArrayList<>();
    private final SimpleBooleanProperty busyProperty = new SimpleBooleanProperty(false);
    
    public ReportBuilder createReportBuilder(int symbolCount) {
        return CallbackReport.builder(this, FullReport.builder(symbolCount));
    }
    
    public void addReportCallback(Callback callback) {
        callbacks.add(callback);
    }

    @Override
    public void onCompute(long date, Report report) {
        callbacks.forEach(callback -> callback.onCompute(date, report));
    }

    @Override
    public void onOpen(MockPosition trade, Report report) {
        callbacks.forEach(callback -> callback.onOpen(trade, report));
    }

    @Override
    public void onClose(MockPosition trade, Report report) {
        callbacks.forEach(callback -> callback.onClose(trade, report));
    }

}
