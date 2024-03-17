package com.bicycle.quant.position;

import com.bicycle.backtest.MockPosition;
import lombok.Getter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Getter
public class PositionModel {
    
    private final ObservableList<MockPosition> positions = FXCollections.observableArrayList();
    
    public void add(MockPosition position) {
        positions.add(position);
    }
    
    public void clear() {
        positions.clear();
    }

}
