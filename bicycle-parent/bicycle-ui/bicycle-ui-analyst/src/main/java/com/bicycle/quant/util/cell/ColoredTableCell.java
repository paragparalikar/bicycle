package com.bicycle.quant.util.cell;

import com.bicycle.quant.Constant;
import java.text.NumberFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColoredTableCell<T, V extends Number> extends SimpleTableCell<T, V> {
    
    public static <T,V extends Number> Callback<TableColumn<T,V>, TableCell<T,V>> factory(
            Number value, NumberFormat numberFormat){
        return tableColumn -> new ColoredTableCell<>(value, numberFormat);
    }

    private final Number value;
    private final NumberFormat numberFormat;
    
    public ColoredTableCell(Number value, NumberFormat numberFormat) {
        super();
        this.value = value;
        this.numberFormat = numberFormat;
    }
    
    @Override
    protected void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || null == item) {
            setText(null);
        } else {
            setText(numberFormat.format(item));
            setTextFill(item.equals(value) ? Constant.COLOR_NEUTRAL : 
                (item.doubleValue() > value.doubleValue() ? Constant.COLOR_POSITIVE : Constant.COLOR_NEGATIVE));
        }
    }
    
}
