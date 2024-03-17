package com.bicycle.core.bar;

import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.Dates;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class BarSeries {
    
    private final Bar[] values;
    @Getter private final Symbol symbol;
    @Getter private final Timeframe timeframe;
    private int size, writePointer;
    
    public BarSeries(Symbol symbol, Timeframe timeframe, int capacity) {
        this.symbol = symbol;
        this.timeframe = timeframe;
        this.values = new Bar[capacity];
        for(int index = 0; index < capacity; index++) {
            this.values[index] = new Bar();
        }
    }
    
    public void add(Bar source) {
        values[writePointer++ % values.length].copy(source);
        if(size < values.length) size++;
    }
    
    public void onTick(long date, float price, int volume) {
        if(0 == size() || Dates.floor(date, timeframe) != Dates.floor(get(0).date(), timeframe)) {
            values[writePointer++ % values.length].add(date, price, volume);
            if(size < values.length) size++;
        } else {
            get(0).add(date, price, volume);
        }
    }
    
    public Bar get(int index) {
        return values[(writePointer - 1 - index) % values.length];
    }
    
    public int size() {
        return size;
    }
    
    public int capacity() {
        return values.length;
    }
    
    public int modCount() {
        return writePointer;
    }
    
    public void clear() {
        size = 0;
        writePointer = 0;
    }
    
    public boolean isEmpty() {
        return 0 == size;
    }

}
