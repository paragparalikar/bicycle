package com.bicycle.backtest.report;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.Strings;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@Getter
public class BaseReport implements Report {
    
    public static ReportBuilder builder(int symbolCount) {
        return (initialMargin, tradingStrategy, startDate, endDate) -> 
            new BaseReport(symbolCount, initialMargin, tradingStrategy, startDate, endDate);
    }
    
    private final long startDate, endDate;
    private int barCount, totalTradeCount;
    private final float initialMargin, years;
    private final MockTradingStrategy tradingStrategy;
    private final Int2ObjectOpenHashMap<MockPosition> openTrades;
    private volatile float availableMargin, equity, maxEquity, minEquity, avgDrawdown, maxDrawdown, exposure;
    
    public BaseReport(
            final int symbolCount,
            float initialMargin, MockTradingStrategy tradingStrategy, 
            ZonedDateTime startDate, ZonedDateTime endDate) {
        this.tradingStrategy = tradingStrategy;
        this.endDate = endDate.toInstant().toEpochMilli();
        this.startDate = startDate.toInstant().toEpochMilli();
        this.years = (this.endDate - this.startDate) / 31536000000f;
        this.openTrades = new Int2ObjectOpenHashMap<>(symbolCount);
        this.maxEquity = this.initialMargin = this.availableMargin = initialMargin;
    }
    
    @Override
    public void clear() {
        openTrades.clear();
        barCount = totalTradeCount = 0;
        avgDrawdown = maxDrawdown = exposure = 0;
        availableMargin = equity = maxEquity = minEquity = initialMargin;
    }
    
    private float computeOpenEquity() {
        return (float) openTrades.values().stream()
            .filter(Objects::nonNull)
            .mapToDouble(MockPosition::getOpenEquity)
            .sum();
    }
    
    @Override
    public void compute(long date) {
        barCount++;
        final float openEquity = computeOpenEquity();
        equity = availableMargin + openEquity;
        maxEquity = Math.max(maxEquity, equity);
        minEquity = Math.min(minEquity, equity);
        final float drawdown = maxEquity > equity ? (maxEquity - equity) / maxEquity : 0;
        maxDrawdown = Math.max(maxDrawdown, drawdown);
        avgDrawdown = (avgDrawdown * (barCount - 1) + drawdown) / barCount;
        exposure = (exposure * (barCount - 1) + Math.abs(openEquity / equity)) / barCount;
    }
    
    @Override
    public MockPosition getOpenPosition(Symbol symbol) {
        return openTrades.get(symbol.token());
    }

    @Override
    public synchronized void open(MockPosition trade) {
        totalTradeCount++;
        openTrades.put(trade.getSymbol().token(), trade);
        availableMargin -= trade.getOpenEquity();
    }

    @Override
    public synchronized void close(MockPosition trade) {
        availableMargin += trade.getCloseEquity();
        openTrades.remove(trade.getSymbol().token());
    }
    
    @Override
    public float getCAGR() {
        double cagr = Math.pow(Math.abs(equity) / initialMargin, 1 / years) - 1;
        cagr = (0 < equity ? cagr : (-1 * cagr -1));
        return (float) cagr;
    }
    
    @Override
    public float getDrawdown() {
        return maxEquity - equity;
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        else return null;
    }
    
    public int getOpenPositionCount() {
        return getOpenTrades().size();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(tradingStrategy.toString()).append("\n");
        builder.append(String.format("Equity initial : %10.2f, max : %10.2f, final : %10.2f\n", initialMargin, maxEquity, equity));
        builder.append("CAGR          : ").append(Strings.format(getCAGR())).append("\n");
        builder.append("Exposure      : ").append(Strings.format(exposure)).append("\n");
        builder.append("RAR           : ").append(Strings.format(getCAGR() / exposure)).append("\n");
        builder.append("AvgDD         : ").append(Strings.format(avgDrawdown)).append("\n");
        builder.append("MaxDD         : ").append(Strings.format(maxDrawdown)).append("\n");
        builder.append("BarCount : ").append(Strings.format(barCount)).append("\n");
        builder.append("PositionCount : ").append(Strings.format(totalTradeCount)).append("\n");
        return builder.toString();
    }
    
}
