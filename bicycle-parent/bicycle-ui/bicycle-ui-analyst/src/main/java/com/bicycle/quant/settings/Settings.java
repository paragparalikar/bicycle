package com.bicycle.quant.settings;

import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluatorType;
import com.bicycle.backtest.strategy.trading.evaluator.robustness.RobustnessEvaluatorType;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Exchange;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.experimental.Accessors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@Getter
@Accessors(fluent = true)
public class Settings {
    
    private final SimpleObjectProperty<Exchange> exchangeProperty = new SimpleObjectProperty<>(Exchange.NSE);
    private final SimpleObjectProperty<Timeframe> timeframeProperty = new SimpleObjectProperty<>(Timeframe.D);
    private final SimpleObjectProperty<LocalDate> toDateProperty = new SimpleObjectProperty<>(LocalDate.now());
    private final SimpleObjectProperty<LocalDate> fromDateProperty = new SimpleObjectProperty<>(LocalDate.now().minusYears(20));
    
    private final SimpleDoubleProperty slippagePercentageProperty = new SimpleDoubleProperty(0.5);
    private final SimpleDoubleProperty initialMarginProperty = new SimpleDoubleProperty(10000000);
    private final SimpleDoubleProperty positionSizePercentageProperty = new SimpleDoubleProperty(1.0);
    private final SimpleBooleanProperty limitToAvailableMarginProperty = new SimpleBooleanProperty(false);
    
    private final SimpleIntegerProperty timeframeSelectionCountProperty = new SimpleIntegerProperty(3);
    private final SimpleIntegerProperty symbolSelectionCountProperty = new SimpleIntegerProperty(100);
    private final SimpleIntegerProperty parameterSelectionCountProperty = new SimpleIntegerProperty(10);
    private final SimpleDoubleProperty outOfSampleRatioProperty = new SimpleDoubleProperty(4);
    private final SimpleIntegerProperty walkForwardStepCountProperty = new SimpleIntegerProperty(10);
    
    private final SimpleObjectProperty<PerformanceEvaluatorType> performanceEvaluatorProperty = new SimpleObjectProperty<>(PerformanceEvaluatorType.RaRByAvgDD);
    private final SimpleObjectProperty<RobustnessEvaluatorType> robustnessEvaluatorProperty = new SimpleObjectProperty<>(RobustnessEvaluatorType.AVERAGE_BY_DEVIATION);
    
    public Exchange getExchange() {
        return exchangeProperty.get();
    }
    
    public Timeframe getTimeframe() {
        return timeframeProperty.get();
    }
    
    public ZonedDateTime getToDate() {
        return ZonedDateTime.of(toDateProperty.get(), LocalTime.MIDNIGHT, ZoneId.systemDefault());
    }

    public ZonedDateTime getFromDate() {
        return ZonedDateTime.of(fromDateProperty.get(), LocalTime.MIDNIGHT, ZoneId.systemDefault());
    }
    
    public double getSlippagePercentage() {
        return slippagePercentageProperty.get();
    }
    
    public double getInitialMargin() {
        return initialMarginProperty.get();
    }
    
    public double getPositionSizePercentage() {
        return positionSizePercentageProperty.get();
    }
    
    public boolean isLimitToAvailableMargin() {
        return limitToAvailableMarginProperty.get();
    }
    
    public int getTimeframeSelectionCount() {
        return timeframeSelectionCountProperty.get();
    }
    
    public int getSymbolSelectionCount() {
        return symbolSelectionCountProperty.get();
    }
    
    public int getParameterSelectionCount() {
        return parameterSelectionCountProperty.get();
    }
    
    public double getOutOfSampleRatio() {
        return outOfSampleRatioProperty.get();
    }
    
    public int getWalkForwardStepCount() {
        return walkForwardStepCountProperty.get();
    }
    
    public PerformanceEvaluatorType getPerformanceEvaluatorType() {
        return performanceEvaluatorProperty.get();
    }
    
    public RobustnessEvaluatorType getRobustnessEvaluatorType() {
        return robustnessEvaluatorProperty.get();
    }
    
}
