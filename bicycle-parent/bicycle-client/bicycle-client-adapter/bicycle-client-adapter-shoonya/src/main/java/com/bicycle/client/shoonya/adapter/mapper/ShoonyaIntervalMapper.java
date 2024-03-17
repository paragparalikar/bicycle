package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.bar.Timeframe;

public class ShoonyaIntervalMapper {

    public int toInterval(Timeframe timeframe) {
        return timeframe.getMinuteMultiple();
    }
    
    public Timeframe toTimeframe(int interval) {
        for(Timeframe timeframe : Timeframe.values()) {
            if(timeframe.getMinuteMultiple() == interval) {
                return timeframe;
            }
        }
        throw new IllegalArgumentException(String.format("Shoonya interval %d is not supported", interval));
    }
    
}
