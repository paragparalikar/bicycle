package com.bicycle.quant;

import com.bicycle.quant.chart.equity.EquityChartPresenter;
import com.bicycle.quant.report.ReportPresenter;
import com.bicycle.quant.script.ScriptPresenter;
import lombok.Getter;

public class MainPresenter {
    
    @Getter private final MainView view;
    
    public MainPresenter(Context context, 
            ScriptPresenter scriptPresenter, 
            ReportPresenter reportPresenter, 
            EquityChartPresenter equityChartPresenter) {
        this.view = new MainView(scriptPresenter.getView());
        context.busyProperty().addListener((o, old, value) -> {
            if(value) view.add(equityChartPresenter.getView(), reportPresenter.getView());
        });
    }
    

}
