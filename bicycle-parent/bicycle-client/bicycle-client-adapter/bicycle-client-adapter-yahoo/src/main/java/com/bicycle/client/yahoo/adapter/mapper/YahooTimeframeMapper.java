package com.bicycle.client.yahoo.adapter.mapper;

import com.bicycle.core.bar.Timeframe;

public class YahooTimeframeMapper {

    public String toYahooTimeframe(Timeframe timeframe) {
        switch(timeframe) {
            case D: return "1d";
            case H1: return "1h";
            case M1: return "1m";
            case M15: return "15m";
            case M30: return "30m";
            case M5: return "5m";
            default: throw new IllegalArgumentException(
                    String.format("Timeframe %s is not supported", timeframe.name()));
        }
    }
    
}
