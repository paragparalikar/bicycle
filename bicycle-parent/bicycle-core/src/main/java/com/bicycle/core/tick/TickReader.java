package com.bicycle.core.tick;

public interface TickReader {
    
    public static TickReader of(Tick tick) {
        return new SingleTickReader(tick);
    }
    
    int size();

    void readInto(Tick tick);
    
}
