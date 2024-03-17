package com.bicycle.backtest;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarListener;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class MockPosition extends Position implements BarListener {
    private static final AtomicInteger ID = new AtomicInteger(0);

    private final int id;
    private float mfe, mae;
    private int barCount, mfeBarCount, maeBarCount;
    
    public MockPosition(Symbol symbol, Timeframe timeframe, OrderType entryType) {
        super(symbol, timeframe, entryType);
        this.id = ID.getAndIncrement();
    }
    
    public float getEtd() {
        return Math.abs(mfe - exitPrice);
    }
    
    public int getEtdBarCount() {
        return barCount - mfeBarCount;
    }
    
    public void onBar(Bar bar) {
        barCount++;
        ltp = bar.close();
    }
    
    public void onPrice(float price) {
        ltp = price;
        final float excursion = price - entryPrice * entryType.multiplier();
        if(excursion > mfe) {
            mfe = excursion;
            mfeBarCount = barCount;
        }
        if(excursion < mae) {
            mae = excursion;
            maeBarCount = barCount;
        }
    }

}
