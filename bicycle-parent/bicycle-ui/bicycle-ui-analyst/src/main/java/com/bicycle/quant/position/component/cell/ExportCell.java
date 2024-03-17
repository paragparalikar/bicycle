package com.bicycle.quant.position.component.cell;

import com.bicycle.backtest.MockPosition;
import com.bicycle.quant.util.Fx;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ExportCell extends TableCell<MockPosition, MockPosition> {
    
    private final Button button = Fx.iconButton(FontAwesomeSolid.CHART_LINE, null);
    
    @Override
    protected void updateItem(MockPosition item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || null == item) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
    
}