package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteInterval;
import com.bicycle.core.bar.Timeframe;

public class KiteIntervalMapper {

    public KiteInterval toKiteInterval(Timeframe timeframe) {
        if(null == timeframe) return null;
        switch(timeframe) {
            case M1: return KiteInterval.M1;
            case M3: return KiteInterval.M3;
            case M5: return KiteInterval.M5;
            case M10: return KiteInterval.M10;
            case M15: return KiteInterval.M15;
            case M30: return KiteInterval.M30;
            case H1: return KiteInterval.H;
            case H2 : return KiteInterval.H2;
            case H3 : return KiteInterval.H3;
            case D: return KiteInterval.D;
            default: throw new IllegalArgumentException(
                    String.format("Timeframe %s is not supported by kite", timeframe.name()));
        }
    }
    
}
