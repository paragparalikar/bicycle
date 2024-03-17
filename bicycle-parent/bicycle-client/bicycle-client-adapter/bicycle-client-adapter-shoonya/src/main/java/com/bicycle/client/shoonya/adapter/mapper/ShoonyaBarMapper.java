package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaBar;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShoonyaBarMapper {

    public Bar toBar(ShoonyaBar bar, Symbol symbol, Timeframe timeframe) {
        return Bar.builder()
                .symbol(symbol)
                .timeframe(timeframe)
                .date(bar.getTime().getTime())
                .open(bar.getOpen())
                .high(bar.getHigh())
                .low(bar.getLow())
                .close(bar.getClose())
                .volume(bar.getVolume())
                .build();
    }
    
}
