package com.bicycle.quant.report;

import com.bicycle.backtest.report.FullReport;
import com.bicycle.quant.Constant;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ReportView extends BorderPane {
    
    private final GridPane container = new GridPane();
    private final Locale locale = Locale.of("en", "IN");
    private final NumberFormat number = NumberFormat.getInstance(locale);
    private final NumberFormat percent = NumberFormat.getPercentInstance(locale);
    
    private Label cagr = label(0, percent);
    private Label rar = label(0, percent);
    private Label exposure = label(50, percent);
    private Label maxDrawdown = label(50, percent);
    private Label averageDrawdown = label(10, percent);
    private Label rarByMaxDD = label(0, number);
    private Label rarByAvgDD = label(0, number);
    private Label rarByAvgAndMaxDD = label(0, number);
    private Label profitFactor = label(0, number);
    private Label payoffRatio = label(0, number);
    private Label expectancy = label(0, number);
    private Label winRate = label();
    
    private Label averageProfit = label();
    private Label maxProfit = label();
    private Label averageLoss = label();
    private Label maxLoss = label();
    
    private Label averageWinningStreak = label();
    private Label maxWinningStreak = label();
    private Label winningStreakCount = label();
    private Label averageLosingStreak = label();
    private Label maxLosingStreak = label();
    private Label losingStreakCount = label();
    
    private Label initialEquity = label();
    private Label minEquity = label();
    private Label maxEquity = label();
    private Label finalEquity = label();
    
    private Label tradeCount = label();
    private Label winningTradeCount = label();
    private Label losingTradeCount = label();
    private Label breakEvenTradeCount = label();
    
    public ReportView() {
        setCenter(new ScrollPane(container));
        layoutLabels();
        
        container.setHgap(20);
        container.setVgap(2);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER_LEFT);
        
        number.setMaximumFractionDigits(3);
        number.setMinimumFractionDigits(0);
        number.setMinimumIntegerDigits(1);
        
        percent.setMaximumFractionDigits(2);
        percent.setMinimumFractionDigits(0);
        percent.setMinimumIntegerDigits(1);
    }
    
    private Label label(String name) {
        final Label label = new Label(name);
        label.setTextFill(Color.BLACK);
        label.setFont(Constant.FONT_NORMAL);
        return label;
    }
    
    private Label label() {
        final Label label = new Label();
        label.setFont(Constant.FONT_BOLD);
        return label;
    }
    
    private Label label(float value, NumberFormat format) {
       final Label label = label();
        label.textFillProperty().bind(new ObjectBinding<Color>() {
            { bind(label.textProperty()); }
            @Override
            protected Color computeValue() {
                try {
                    if(null != label.getText()) {
                        return value < format.parse(label.getText()).floatValue() ? 
                                    Constant.COLOR_POSITIVE : 
                                    Constant.COLOR_NEGATIVE;
                    }
                    return Constant.COLOR_NEUTRAL;
                } catch (ParseException e) {
                    return Constant.COLOR_NEUTRAL;
                }
            }
        });
        return label;
    }
    
    private void layoutLabels() {
        container.addRow(0, label("CAGR"), cagr);
        container.addRow(1, label("RaR"), rar);
        container.addRow(2, label("Exposure"), exposure);
        container.addRow(3, label("Max Drawdown"), maxDrawdown);
        container.addRow(4, label("Avg Drawdown"), averageDrawdown);
        container.addRow(5, label("RaRByMaxDD"), rarByMaxDD);
        container.addRow(6, label("RaRByAvgDD"), rarByAvgDD);
        container.addRow(7, label("RaRByMaxAndAvgDD"), rarByAvgAndMaxDD);
        container.addRow(8, label("Profit Factor"), profitFactor);
        container.addRow(9, label("Payoff Ratio"), payoffRatio);
        container.addRow(10, label("Expectancy"), expectancy);
        container.addRow(11, label("Win Rate"), winRate);
        container.add(new Separator(), 0, 12, 2, 1);
        
        container.addRow(13, label("Avg Profit"), averageProfit);
        container.addRow(14, label("Max Profit"), maxProfit);
        container.addRow(15, label("Avg Loss"), averageLoss);
        container.addRow(16, label("Max Loss"), maxLoss);
        container.add(new Separator(), 0, 17, 2, 1);
        
        container.addRow(18, label("Avg Win Streak"), averageWinningStreak);
        container.addRow(19, label("Max Win Streak"), maxWinningStreak);
        container.addRow(20, label("Win Streak Count"), winningStreakCount);
        container.addRow(21, label("Avg Loss Streak"), averageLosingStreak);
        container.addRow(22, label("Max Loss Streak"), maxLosingStreak);
        container.addRow(23, label("Loss Streak Count"), losingStreakCount);
        container.add(new Separator(), 0, 24, 2, 1);
        
        container.addRow(25, label("Initial Equity"), initialEquity);
        container.addRow(26, label("Min Equity"), minEquity);
        container.addRow(27, label("Max Equity"), maxEquity);
        container.addRow(28, label("Final Equity"), finalEquity);
        container.add(new Separator(), 0, 29, 2, 1);
        
        container.addRow(30, label("Trade Count"), tradeCount);
        container.addRow(31, label("Win Count"), winningTradeCount);
        container.addRow(32, label("Loss Count"), losingTradeCount);
        container.addRow(33, label("Breakeven Count"), breakEvenTradeCount);
    }
    
    public void show(FullReport report) {
        cagr.setText(percent.format(report.getCAGR()));
        rar.setText(percent.format(report.getCAGR() / report.getExposure()));
        exposure.setText(percent.format(report.getExposure()));
        maxDrawdown.setText(percent.format(report.getMaxDrawdown()));
        averageDrawdown.setText(percent.format(report.getAvgDrawdown()));
        rarByMaxDD.setText(number.format(report.getCAGR() * 100 / (report.getExposure() * report.getMaxDrawdown())));
        rarByAvgDD.setText(number.format(report.getCAGR() * 100 / (report.getExposure() * report.getAvgDrawdown())));
        rarByAvgAndMaxDD.setText(number.format(report.getCAGR() * 100 / (report.getExposure() * report.getAvgDrawdown() * report.getMaxDrawdown())));
        profitFactor.setText(number.format(report.getProfitFactor()));
        payoffRatio.setText(number.format(report.getPayoffRatio()));
        expectancy.setText(number.format(report.getExpectancy()));
        winRate.setText(percent.format(((double)report.getWinningTradeCount()) / ((double)report.getTotalTradeCount())));
        averageProfit.setText(percent.format(report.getAvgProfit() / 100));
        maxProfit.setText(percent.format(report.getMaxProfit() / 100));
        averageLoss.setText(percent.format(report.getAvgLoss() / 100));
        maxLoss.setText(percent.format(report.getMaxLoss() / 100));
        averageWinningStreak.setText(number.format(report.getAvgWinningStreak()));
        maxWinningStreak.setText(number.format(report.getMaxWinningStreak()));
        winningStreakCount.setText(number.format(report.getWinningStreakCount()));
        averageLosingStreak.setText(number.format(report.getAvgLosingStreak()));
        maxLosingStreak.setText(number.format(report.getMaxLosingStreak()));
        losingStreakCount.setText(number.format(report.getLosingStreakCount()));
        initialEquity.setText(number.format(report.getInitialMargin()));
        maxEquity.setText(number.format(report.getMaxEquity()));
        minEquity.setText(number.format(report.getMinEquity()));
        finalEquity.setText(number.format(report.getEquity()));
        tradeCount.setText(number.format(report.getTotalTradeCount()));
        winningTradeCount.setText(number.format(report.getWinningTradeCount()));
        losingTradeCount.setText(number.format(report.getLosingTradeCount()));
        breakEvenTradeCount.setText(number.format(report.getTotalTradeCount() - report.getOpenPositionCount() 
                - report.getWinningTradeCount() - report.getLosingTradeCount()));
    }
    
    public void clear() {
        cagr.setText(null);
        rar.setText(null);
        exposure.setText(null);
        maxDrawdown.setText(null);
        averageDrawdown.setText(null);
        rarByMaxDD.setText(null);
        rarByAvgDD.setText(null);
        rarByAvgAndMaxDD.setText(null);
        profitFactor.setText(null);
        payoffRatio.setText(null);
        expectancy.setText(null);
        winRate.setText(null);
        averageProfit.setText(null);
        maxProfit.setText(null);
        averageLoss.setText(null);
        maxLoss.setText(null);
        averageWinningStreak.setText(null);
        maxWinningStreak.setText(null);
        winningStreakCount.setText(null);
        averageLosingStreak.setText(null);
        maxLosingStreak.setText(null);
        losingStreakCount.setText(null);
        initialEquity.setText(null);
        maxEquity.setText(null);
        minEquity.setText(null);
        finalEquity.setText(null);
        tradeCount.setText(null);
        winningTradeCount.setText(null);
        losingTradeCount.setText(null);
        breakEvenTradeCount.setText(null);
    }
    
}
