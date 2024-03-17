package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.Constant;
import com.bicycle.client.kite.api.model.KiteCandle;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.util.Dates;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class KiteBarMapper {

    public Bar toBar(Symbol symbol, Timeframe timeframe, KiteCandle candle) {
        if(null == candle) return null;
        
        long date = candle.getTimestamp().toInstant().toEpochMilli();
        if(candle.getTimestamp().toLocalTime().isBefore(Constant.NSE_START_TIME)) {
            date = ZonedDateTime.of(candle.getTimestamp().toLocalDate(), Constant.NSE_START_TIME, 
                    ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        date = Dates.floor(date, timeframe);
        
        return Bar.builder()
                .date(date)
                .symbol(symbol)
                .timeframe(timeframe)
                .volume(candle.getVolume())
                .open(candle.getOpen())
                .high(candle.getHigh())
                .low(candle.getLow())
                .close(candle.getClose())
                .build();
    }
    
}
