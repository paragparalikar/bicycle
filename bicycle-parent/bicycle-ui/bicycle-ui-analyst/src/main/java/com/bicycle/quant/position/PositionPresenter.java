package com.bicycle.quant.position;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.CallbackReport.Callback;
import com.bicycle.backtest.report.Report;
import com.bicycle.quant.Context;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public class PositionPresenter implements Callback, ChangeListener<Boolean> {
    
    private PositionView view;
    private final PositionModel model = new PositionModel();
    
    public PositionPresenter(Context context) {
        context.addReportCallback(this);
    }
    
    public Node getView() {
        if(null == view) {
            view = new PositionView();
            view.getTableView().setItems(model.getPositions());
        }
        return view;
    }
    
    @Override
    public void onOpen(MockPosition position, Report report) {
        model.add(position);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if(newValue) model.clear();
    }

}
