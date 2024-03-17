package com.bicycle.quant;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainView extends BorderPane {
    
    private final Node scriptView;
    
    public MainView(Node scriptView) {
        setCenter(this.scriptView = scriptView);
    }
    
    void add(Node equityChartView, Node reportView) {
        final StackPane equityChartPane = new StackPane(equityChartView);
        StackPane.setMargin(equityChartView, new Insets(10, 0, 0, 10));
        final SplitPane verticalSplitPane = new SplitPane(scriptView, equityChartPane);
        verticalSplitPane.setOrientation(Orientation.VERTICAL);
        verticalSplitPane.setDividerPositions(0.35);
        
        final SplitPane horizontalSplitPane = new SplitPane(verticalSplitPane, reportView);
        horizontalSplitPane.setDividerPositions(0.8);
        setCenter(horizontalSplitPane);
    }
    
}
