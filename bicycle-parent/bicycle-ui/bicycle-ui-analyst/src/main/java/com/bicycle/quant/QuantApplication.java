package com.bicycle.quant;

import com.bicycle.quant.chart.equity.EquityChartPresenter;
import com.bicycle.quant.menu.MenuBarPresenter;
import com.bicycle.quant.menu.MenuBarView;
import com.bicycle.quant.position.PositionPresenter;
import com.bicycle.quant.report.ReportPresenter;
import com.bicycle.quant.script.ScriptPresenter;
import com.bicycle.quant.settings.SettingsPresenter;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class QuantApplication extends Application {
    
    @Override
    @SuppressWarnings("unused") 
    public void start(Stage stage) throws Exception {
        
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        
        final ModalPane modalPane = new ModalPane();
        final Context context = new Context(stage, modalPane);
        final MenuBarView menuBarView = new MenuBarView();
        final ReportPresenter reportPresenter = new ReportPresenter(context);
        final PositionPresenter positionPresenter = new PositionPresenter(context);
        final SettingsPresenter settingsPresenter = new SettingsPresenter(context);
        final ScriptPresenter scriptPresenter = new ScriptPresenter(menuBarView, context);
        final EquityChartPresenter equityChartPresenter = new EquityChartPresenter(context);
        final MenuBarPresenter menuBarPresenter = new MenuBarPresenter(context, menuBarView, 
                scriptPresenter, positionPresenter, settingsPresenter);
        final MainPresenter mainPresenter = new MainPresenter(context, scriptPresenter, reportPresenter, equityChartPresenter);
        
        final Scene scene = new Scene(new StackPane(modalPane, mainPresenter.getView()));
        scene.getStylesheets().addAll("styles/root.css", "styles/formsfx.css", "styles/editor.css", "styles/override.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }

}
