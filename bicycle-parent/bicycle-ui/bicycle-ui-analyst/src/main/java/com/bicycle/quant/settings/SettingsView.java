package com.bicycle.quant.settings;

import com.bicycle.backtest.strategy.trading.evaluator.performance.PerformanceEvaluatorType;
import com.bicycle.backtest.strategy.trading.evaluator.robustness.RobustnessEvaluatorType;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Exchange;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleDateField;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.view.PreferencesFxDialog;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.MasterDetailPane;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

@RequiredArgsConstructor
public class SettingsView {
    
    private PreferencesFx editor;
    private final Settings settings;
    
    public PreferencesFx getEditor() {
        return null == editor ? editor = buildView() : editor;
    }
    
    private PreferencesFx buildView()  {
        final ListProperty<PerformanceEvaluatorType> performanceEvaluatorTypesProperty = 
                new SimpleListProperty<>(FXCollections.observableArrayList(PerformanceEvaluatorType.values()));
        final ListProperty<RobustnessEvaluatorType> robustnessEvaluatorTypesProperty = 
                new SimpleListProperty<>(FXCollections.observableArrayList(RobustnessEvaluatorType.values()));
        final ListProperty<Exchange> exchangesProperty = 
                new SimpleListProperty<>(FXCollections.observableArrayList(Exchange.values()));
        final ListProperty<Timeframe> timeframesProperty = 
                new SimpleListProperty<>(FXCollections.observableArrayList(Timeframe.values()));
        
        final PreferencesFx editor = PreferencesFx.of(getClass(), 
                Category.of("DataSet Selection",
                        Setting.of("Exchange", exchangesProperty, settings.exchangeProperty()),
                        Setting.of("Timeframe", timeframesProperty, settings.timeframeProperty()),
                        Setting.of("From Date", new SimpleDateField(settings.fromDateProperty()), settings.fromDateProperty()),
                        Setting.of("To Date", new SimpleDateField(settings.toDateProperty()), settings.toDateProperty())),
                Category.of("Backtest Properties", 
                        Setting.of("Initial Margin", settings.initialMarginProperty()),
                        Setting.of("Slippage Percentage", settings.slippagePercentageProperty()),
                        Setting.of("Position Size Percentage", settings.positionSizePercentageProperty()),
                        Setting.of("Limit Exposure To Available Margin", settings.limitToAvailableMarginProperty())),
                Category.of("Optimization Properties",
                        Setting.of("Performance Evaluator", performanceEvaluatorTypesProperty, settings.performanceEvaluatorProperty()),
                        Setting.of("Robustness Evaluator", robustnessEvaluatorTypesProperty, settings.robustnessEvaluatorProperty()), 
                        Setting.of("Timeframe Selection Count", settings.timeframeSelectionCountProperty()),
                        Setting.of("Symbol Selection Count", settings.symbolSelectionCountProperty()),
                        Setting.of("Parameter Selection Count", settings.parameterSelectionCountProperty())),
                Category.of("Analysis Properties",
                        Setting.of("Out Of Sample Ratio", settings.outOfSampleRatioProperty()),
                        Setting.of("WalkForward Step Count", settings.walkForwardStepCountProperty())))
                .dialogTitle("Settings");
        
        final PreferencesFxDialog dialog = editor.getPreferencesFxDialog();
        dialog.setMinWidth(600);
        dialog.setMaxHeight(400);
        
        final MasterDetailPane masterDetailPane = (MasterDetailPane) editor.getView().getCenter();
        masterDetailPane.setDividerPosition(0.33);
        
        return editor;
    }

}
