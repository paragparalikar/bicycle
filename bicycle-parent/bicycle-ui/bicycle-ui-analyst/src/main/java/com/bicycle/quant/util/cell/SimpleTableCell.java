package com.bicycle.quant.util.cell;

import com.bicycle.quant.Constant;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SimpleTableCell<T,V> extends TableCell<T,V> {
    
    public static <T,V> Callback<TableColumn<T,V>, TableCell<T,V>> factory(){
        return tableColumn -> new SimpleTableCell<>();
    }

    public SimpleTableCell() {
        setFont(Constant.FONT_NORMAL);
    }
    
    @Override
    protected void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || null == item) {
            setText(null);
        } else {
            setText(String.valueOf(item));
        }
    }
    
}
