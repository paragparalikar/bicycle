package com.bicycle.core.position;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.symbol.Symbol;
import java.util.Date;
import lombok.Data;

@Data
public class Position {

    protected final Symbol symbol;
    protected final Timeframe timeframe;
    protected final OrderType entryType;
    protected long entryDate;
    protected int entryQuantity;
    protected float entryPrice, entryAtr, ltp;
    protected long exitDate;
    protected float exitPrice;
    protected int exitQuantity;
    
    public void enter(long entryDate, int entryQuantity, float entryPrice) {
        this.entryDate = entryDate;
        this.entryQuantity = entryQuantity;
        this.entryPrice = entryPrice;
    }
    
    public void exit(long exitDate, int exitQuantity, float exitPrice) {
        this.exitDate = exitDate;
        this.exitQuantity = exitQuantity;
        this.exitPrice = exitPrice;
    }
    
    public float getClosePercentProfitLoss() {
        return entryType.multiplier() * (exitQuantity * exitPrice - entryQuantity * entryPrice) * 100 / (entryQuantity * entryPrice);
    }
    
    public float getOpenPercentProfitLoss() {
        return entryType.multiplier() * (entryQuantity * (0 == ltp ? entryPrice : ltp) - entryQuantity * entryPrice) * 100 / (entryQuantity * entryPrice);
    }
    
    public long getDuration() {
        return exitDate - entryDate;
    }
    
    public float getOpenEquity() {
        return entryType.multiplier() * (0 == ltp ? entryPrice : ltp) * entryQuantity;
    }
    
    public float getCloseEquity() {
        return entryType.multiplier() * exitPrice * exitQuantity;
    }
    
    public boolean isOpen() {
        return entryQuantity != exitQuantity;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(entryType.name()).append(",");
        builder.append(symbol.code()).append(",");
        builder.append(timeframe.name()).append(",");
        builder.append(new Date(entryDate)).append(",");
        builder.append(entryPrice).append(",");
        builder.append(entryQuantity).append(",");
        builder.append(new Date(exitDate)).append(",");
        builder.append(exitPrice).append(",");
        builder.append(exitQuantity);
        return builder.toString(); 
    }
    
}
