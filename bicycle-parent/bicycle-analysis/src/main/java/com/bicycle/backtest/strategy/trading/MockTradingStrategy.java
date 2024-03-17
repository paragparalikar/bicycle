package com.bicycle.backtest.strategy.trading;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.report.Report;
import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.positionSizing.PositionSizingStrategy;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarListener;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.indicator.Indicator;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.rule.Rule;
import com.bicycle.core.symbol.Symbol;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

public class MockTradingStrategy implements BarListener {
    private static final AtomicInteger ID = new AtomicInteger(0);

    private String text;
    @Getter private final int id;
    private final float slippagePercentage;
    private final ReportCache reportCache;
    private Rule entryRule, exitRule;
    private final Indicator atrIndicator;
    @Getter private final OrderType entryOrderType;
    private final PositionSizingStrategy positionSizingStrategy;
    
    public MockTradingStrategy(float slippagePercentage,
            Rule entryRule, Rule exitRule, OrderType entryOrderType,
            ReportCache reportCache, Indicator atrIndicator,
            PositionSizingStrategy positionSizingStrategy) {
        this.entryRule = entryRule;
        this.exitRule = exitRule;
        this.entryOrderType = entryOrderType;
        this.reportCache = reportCache;
        this.atrIndicator = atrIndicator;
        this.id = ID.getAndIncrement();
        this.slippagePercentage = slippagePercentage;
        this.positionSizingStrategy = positionSizingStrategy;
    }
    
    @Override
    public void onBar(Bar bar) {
        MockPosition openTrade = reportCache.get(bar.symbol(), this).getOpenPosition(bar.symbol());
        if(null != openTrade) {
            openTrade.onPrice(bar.low());   // Update MAE, MFE
            openTrade.onPrice(bar.high());  // Update MAE, MFE
            openTrade.onBar(bar);           // Update LTP
            if(tryExit(bar, openTrade)) {
                reportCache.get(bar.symbol(), this).close(openTrade);
                openTrade = null;
            }
        }
        if(null == openTrade && null != (openTrade = tryEnter(bar))) {
            openTrade.setEntryAtr(atrIndicator.getValue(bar.symbol(), bar.timeframe()));
            reportCache.get(bar.symbol(), this).open(openTrade);
        }
    }
    
    public void onTick(Symbol symbol, Timeframe timeframe, long date, float price, int volume) {
        
    }
    
    public MockPosition tryEnter(Bar bar) {
        if(entryRule.isSatisfied(bar.symbol(), bar.timeframe(), null)) {
            final MockPosition trade = new MockPosition(bar.symbol(), bar.timeframe(), entryOrderType);
            final float entryPrice = bar.close() * (100 + entryOrderType.multiplier() * slippagePercentage) / 100;
            final Report report = reportCache.get(bar.symbol(), this);
            final int entryQuantity = positionSizingStrategy.size(entryPrice, report.getInitialMargin(), report.getAvailableMargin(), trade);
            if(0 < entryQuantity) {
                trade.enter(bar.date(), entryQuantity, entryPrice);
                return trade;
            }
        }
        return null;
    }
    
    public boolean tryExit(Bar bar, MockPosition trade) {
        if(exitRule.isSatisfied(trade.getSymbol(), trade.getTimeframe(), trade)) {
            if(0 == trade.getExitPrice()) trade.setExitPrice(bar.close());
            final float exitPrice = trade.getExitPrice() * (100 + entryOrderType.complement().multiplier() * slippagePercentage) / 100;
            trade.exit(bar.date(), trade.getEntryQuantity(), exitPrice);
            return true;
        }
        return false;
    }
    
    public float distance(MockTradingStrategy tradingStrategy) {
        return entryRule.distance(tradingStrategy.entryRule) + exitRule.distance(tradingStrategy.exitRule);
    }
    
    public void addEntryRule(Rule rule) {
        entryRule = entryRule.and(rule);
    }
    
    public void addExitRule(Rule rule) {
        exitRule = exitRule.or(rule);
    }
    
    @Override
    public String toString() {
        return null == text ? text = new StringBuilder()
                .append(entryOrderType).append(" ").append(entryRule).append(" : ")
                .append(entryOrderType.complement()).append(" ").append(exitRule)
                .toString() : text;
    }
}
