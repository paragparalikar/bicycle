package com.bicycle.core.tick;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingleTickReader implements TickReader {
    
    private final Tick tick;
    
    @Override
    public int size() {
        return 1;
    }

    @Override
    public void readInto(Tick tick) {
        tick.copy(this.tick);
    }

}
