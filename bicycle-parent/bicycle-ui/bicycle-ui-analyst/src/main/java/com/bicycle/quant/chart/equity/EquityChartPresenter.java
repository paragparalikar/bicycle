package com.bicycle.quant.chart.equity;

import com.bicycle.backtest.report.CallbackReport.Callback;
import com.bicycle.backtest.report.Report;
import com.bicycle.quant.Context;

public class EquityChartPresenter implements Callback {
    
    private EquityChartView view;
    private final EquityChartModel model = new EquityChartModel();
    
    public EquityChartPresenter(Context context) {
        context.addReportCallback(this);
        context.busyProperty().addListener((o, old, value) -> {
            if(value) clear();
        });
    }
    
    public void clear() {
        model.clear();
        getView().invalidate();
    }
    
    public EquityChartView getView() {
        return null == view ? view = new EquityChartView(model.getEquityDataSet()) : view;
    }
    
    @Override
    public void onCompute(long date, Report report) {
        model.add(date, report.getEquity());
    }

}
