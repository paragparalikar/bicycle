package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaTick;
import com.bicycle.core.tick.Tick;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShoonyaTickMapper {
    
    private final ShoonyaSymbolMapper shoonyaSymbolMapper;

    public Tick toTick(ShoonyaTick shoonyaTick) {
        if(null == shoonyaTick) return null;
        return Tick.builder()
                .symbol(shoonyaSymbolMapper.getSymbol(shoonyaTick.getExchange(), shoonyaTick.getToken()))
                .date(System.currentTimeMillis())
                .ltp(shoonyaTick.getLastTradedPrice())
                //.open(shoonyaTick.getOpenPrice())
                //.high(shoonyaTick.getHighPrice())
                //.low(shoonyaTick.getLowPrice())
                //.close(shoonyaTick.getClosePrice())
                //.avgTradePrice(shoonyaTick.getAverageTradePrice())
                .volume(shoonyaTick.getVolume())
                .build();
    }
    
}
