package com.bicycle.core.bar;

import com.bicycle.core.symbol.Symbol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"symbol", "timeframe", "date"})
public class Bar {
    
    private Symbol symbol;
    private Timeframe timeframe;
    private long date;
    private float open, high, low, close;
    private int volume;
    
    public void multiply(float multiplier) {
        open = open * multiplier;
        high = high * multiplier;
        low = low * multiplier;
        close = close * multiplier;
        volume = (int) (volume / multiplier);
    }
    
    public void add(long date, float ltp, int volume) {
        this.date = date;
        this.close = ltp;
        this.open = 0 == open ? ltp : open;
        this.low = 0 == low || low > ltp ? ltp : low;
        this.high = 0 == high || high < ltp ? ltp : high;
        this.volume += volume;
    }
    
    public void copy(Bar source) {
        this.symbol = source.symbol;
        this.timeframe = source.timeframe;
        this.date = source.date;
        this.open = source.open;
        this.high = source.high;
        this.low = source.low;
        this.close = source.close;
        this.volume = source.volume;
    }
    
}
