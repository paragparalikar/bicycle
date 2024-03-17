package com.bicycle.client.yahoo.adapter.mapper;

import com.bicycle.client.yahoo.model.YahooBar;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;

public class YahooBarMapper {

    public Bar toBar(Symbol symbol, Timeframe timeframe, YahooBar yahooBar) {
        return Bar.builder()
                .symbol(symbol)
                .timeframe(timeframe)
                .date(yahooBar.date())
                .open(yahooBar.open())
                .high(yahooBar.high())
                .low(yahooBar.low())
                .close(yahooBar.close())
                .volume(yahooBar.volume())
                .build();
    }
    
}
