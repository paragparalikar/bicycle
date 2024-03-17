package com.bicycle.core.tick;

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
@EqualsAndHashCode(of = {"symbol", "date"})
public class Tick {
    public static final int BYTES = 
            Byte.BYTES          // Symbol.exchange.ordinal
            + Integer.BYTES     // Symbol.token
            + Long.BYTES        // date
            + Float.BYTES       // ltp
            + Integer.BYTES;    // volume
    
    private Symbol symbol;
    private long date;
    private float ltp;
    private int volume;
    
    public void copy(Tick source) {
        this.symbol = source.symbol;
        this.date = source.date;
        this.ltp = source.ltp;
        this.volume = source.volume;
    }
    
}

// float open, float high, float low, float close, float avgTradePrice, 
// Above information is available at both Kite and Shoonya brokers.
// It can be added in future, if needed.
