package com.bicycle.quant.menu;

import com.bicycle.quant.Context;
import com.bicycle.quant.position.PositionPresenter;
import com.bicycle.quant.script.ScriptPresenter;
import com.bicycle.quant.settings.SettingsPresenter;
import lombok.Getter;
import javafx.event.ActionEvent;


public class MenuBarPresenter {
    
    private final Context context;
    @Getter private final MenuBarView view;
    private final ScriptPresenter scriptPresenter;
    private final PositionPresenter positionPresenter;
    private final SettingsPresenter settingsPresenter;
    
    
    public MenuBarPresenter(
            Context context,
            MenuBarView view,
            ScriptPresenter scriptPresenter,
            PositionPresenter positionPresenter,
            SettingsPresenter settingsPresenter) {
        this.view = view;
        this.context = context;
        this.scriptPresenter = scriptPresenter;
        this.positionPresenter = positionPresenter;
        this.settingsPresenter = settingsPresenter;
        view.getPositionsMenuItem().setOnAction(this::showPositionView);
        view.getPreferencesMenuItem().setOnAction(this::showPreferencesView);
        view.getExecuteMenuItem().setOnAction(this::execute);
    }
    
    private void showPositionView(ActionEvent event) {
        context.modalPane().show(positionPresenter.getView());
    }
    
    private void showPreferencesView(ActionEvent event) {
        settingsPresenter.show();
    }
    
    private void execute(ActionEvent event) {
        scriptPresenter.execute();
    }

}
