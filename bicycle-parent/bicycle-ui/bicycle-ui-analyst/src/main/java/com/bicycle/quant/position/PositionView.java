package com.bicycle.quant.position;

import com.bicycle.backtest.MockPosition;
import com.bicycle.quant.Constant;
import com.bicycle.quant.position.component.cell.ExportCell;
import com.bicycle.quant.util.Fx;
import com.bicycle.quant.util.ObjectConstant;
import com.bicycle.quant.util.cell.ColoredTableCell;
import com.bicycle.quant.util.cell.SimpleTableCell;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;
import lombok.Getter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import atlantafx.base.theme.Styles;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class PositionView extends BorderPane {
    
    @Getter private final TableView<MockPosition> tableView = new TableView<>();
    private final NumberFormat integerFormat = NumberFormat.getIntegerInstance(Constant.LOCALE);
    private final NumberFormat percentFormat = NumberFormat.getPercentInstance(Constant.LOCALE);
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Constant.LOCALE);
    private final DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd hh:mm");
    
    public PositionView() {
        setCenter(tableView);
        percentFormat.setMaximumFractionDigits(2);
        percentFormat.setMinimumFractionDigits(0);
        percentFormat.setMinimumIntegerDigits(1);
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(0);
        currencyFormat.setMinimumIntegerDigits(1);
        tableView.getStyleClass().addAll(Styles.DENSE, "edge-to-edge");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        
        tableView.getColumns().add(createColumn("Symbol", position -> position.getSymbol().name()));
        tableView.getColumns().add(createColumn("Timeframe", position -> position.getTimeframe().getDisplayText()));
        tableView.getColumns().add(createColumn("Entry Date", position -> dateFormat.format(new Date(position.getEntryDate()))));
        tableView.getColumns().add(createColumn("Entry Price", position -> currencyFormat.format(position.getEntryPrice())));
        tableView.getColumns().add(createColumn("Entry Quantity", position -> integerFormat.format(position.getEntryQuantity())));
        tableView.getColumns().add(createColumn("Exit Date", position -> dateFormat.format(new Date(position.getExitDate()))));
        tableView.getColumns().add(createColumn("Exit Price", position -> currencyFormat.format(position.getExitPrice())));
        tableView.getColumns().add(createColumn("Exit Quantity", position -> integerFormat.format(position.getExitQuantity())));
        
        final TableColumn<MockPosition, Number> profitLossColumn = new TableColumn<>("Profit/Loss %");
        profitLossColumn.setCellValueFactory(this::profitLossValue);
        profitLossColumn.setCellFactory(ColoredTableCell.factory(0, percentFormat));
        tableView.getColumns().add(profitLossColumn);
        
        tableView.getColumns().add(createColumn("Bar Count", position -> integerFormat.format(position.getBarCount())));
        
        tableView.getColumns().add(createActionColumn());
    }
    
    private ObservableValue<Number> profitLossValue(CellDataFeatures<MockPosition, Number> features) {
        final MockPosition position = features.getValue();
        final Number value = position.isOpen() ? position.getOpenPercentProfitLoss() : position.getClosePercentProfitLoss();
        return new ObjectConstant<>(value.doubleValue() / 100); // percentFormat will multiply by 100
    }
    
    private TableColumn<MockPosition, String> createColumn(String name, Function<MockPosition, String> function){
        final TableColumn<MockPosition, String> column = new TableColumn<>(name);
        column.setCellFactory(SimpleTableCell.factory());
        column.setCellValueFactory(features -> new ObjectConstant<>(function.apply(features.getValue())));
        return column;
    }
    
    private TableColumn<MockPosition, MockPosition> createActionColumn(){
        final TableColumn<MockPosition, MockPosition> column = new TableColumn<>();
        final Button exportButton = Fx.iconButton(FontAwesomeSolid.FILE_EXPORT, null);
        column.setGraphic(exportButton);
        column.setCellFactory(c -> new ExportCell());
        column.setCellValueFactory(features -> new ObjectConstant<>(features.getValue()));
        column.setMaxWidth(60);
        return column;
    }

}
