package com.bicycle.quant.settings;

import com.bicycle.quant.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SettingsPresenter {

    private SettingsView view;
    private final Context context;
        
    public void show() {
        if(null == view) view = new SettingsView(context.settings());
        view.getEditor().show();
    }

}
