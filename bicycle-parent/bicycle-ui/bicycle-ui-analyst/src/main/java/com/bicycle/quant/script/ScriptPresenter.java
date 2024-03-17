package com.bicycle.quant.script;

import com.bicycle.quant.Context;
import java.util.concurrent.ForkJoinPool;
import lombok.Getter;
import javafx.scene.control.MenuBar;

public class ScriptPresenter {

    private final Context context;
    @Getter private final ScriptView view;
    
    public ScriptPresenter(MenuBar menuBar, Context context) {
        this.context = context;
        this.view = new ScriptView(menuBar, context.stage());
    }
    
    public void execute() {
        context.busyProperty().set(true);
        ForkJoinPool.commonPool().submit(new ScriptExecutionWorker(view.getText(), context));
    }
    
}
