package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteTick;
import com.bicycle.core.tick.Tick;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KiteTickMapper {
    
    private final KiteSymbolMapper kiteSymbolMapper;
    
    public void copy(Tick tick, KiteTick kiteTick) {
        tick.ltp(kiteTick.getLastTradedPrice());
        tick.date(kiteTick.getLastTradedTime());
        tick.volume(kiteTick.getVolumeTradedToday());
        tick.symbol(kiteSymbolMapper.getSymbol(kiteTick.getToken()));
    }

    public Tick toTick(KiteTick kiteTick) {
        if(null == kiteTick) return null;
        return Tick.builder()
                .symbol(kiteSymbolMapper.getSymbol(kiteTick.getToken()))
                .date(kiteTick.getLastTradedTime())
                .ltp(kiteTick.getLastTradedPrice())
                //.open(kiteTick.getOpenPrice())
                //.high(kiteTick.getHighPrice())
                //.low(kiteTick.getLowPrice())
                //.close(kiteTick.getClosePrice())
                .volume(kiteTick.getVolumeTradedToday())
                //.avgTradePrice(kiteTick.getAverageTradePrice())
                .build();
    }
    
}
