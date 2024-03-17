package com.bicycle.quant.script;

import com.bicycle.quant.script.editor.ScriptEditor;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScriptView extends BorderPane {
    
    private final ScriptEditor editor;
    
    public ScriptView(MenuBar menuBar, Stage stage) {
        this.editor = new ScriptEditor(stage);
        setTop(menuBar);
        setCenter(editor);
        editor.appendText("""   
smaItr = optimize(20,10,100,10) 
prevItr = optimize(1,1,21,3) 
buy ((close > sma(close, smaItr)) and (close > previous(high, prevItr)) 
sell ((close < sma(close, smaItr)) or (close < previous(low, prevItr)) 
        """);
    }
    
    public String getText() {
        return editor.getText();
    }
    
}
