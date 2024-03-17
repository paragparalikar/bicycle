package com.bicycle.client.yahoo.adapter.mapper;

import com.bicycle.core.symbol.Exchange;

public class YahooExchangeMapper {

    public String toYahooExchange(Exchange exchange) {
        switch(exchange) {
            case NSE: return ".NS";
            default:throw new IllegalArgumentException(
                    String.format("Exchange %s is not supported", exchange.name()));
        }
    }
    
}
