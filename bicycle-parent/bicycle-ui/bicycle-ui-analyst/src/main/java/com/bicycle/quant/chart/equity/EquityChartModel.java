package com.bicycle.quant.chart.equity;

import io.fair_acc.dataset.spi.DoubleDataSet;
import lombok.Getter;

@Getter
public class EquityChartModel {
    
    private final DoubleDataSet equityDataSet = new DoubleDataSet("Equity");
    
    public void add(double x, double y) {
        equityDataSet.add(x, y);
    }
    
    public void clear() {
        equityDataSet.clearData();
    }

}
