package com.bicycle.quant.chart.equity;

import com.bicycle.quant.util.converter.DateStringConverter;
import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.ui.geometry.Side;
import io.fair_acc.dataset.DataSet;

public class EquityChartView extends XYChart {
    
    public EquityChartView(DataSet equityDataSet) {
        getAxes().addAll(createDateAxis(), createEquityAxis());
        getDatasets().add(equityDataSet);
        setLegendVisible(false);
    }
    
    private DefaultNumericAxis createDateAxis() {
        final DefaultNumericAxis dateAxis = new DefaultNumericAxis("Date");
        dateAxis.setAnimated(false);
        dateAxis.setForceZeroInRange(false);
        dateAxis.setTickLabelFormatter(new DateStringConverter());
        return dateAxis;
    }
    
    private DefaultNumericAxis createEquityAxis() {
        final DefaultNumericAxis equityAxis = new DefaultNumericAxis("Equity");
        equityAxis.setSide(Side.RIGHT);
        equityAxis.setAnimated(false);
        equityAxis.setAutoUnitScaling(true);
        equityAxis.setForceZeroInRange(false);
        return equityAxis;
    }
    
}