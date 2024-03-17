package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.core.symbol.Exchange;

public class ShoonyaExchangeMapper {

    public Exchange toExchange(ShoonyaExchange shoonyaExchange) {
        if(null == shoonyaExchange) return null;
        switch(shoonyaExchange) {
            case BSE: return Exchange.BSE;
            case CDS: return Exchange.CDS;
            case MCX: return Exchange.MCX;
            case NFO: return Exchange.NFO;
            case NSE: return Exchange.NSE;
            default: throw new IllegalArgumentException(String.format("SoonyaExchange %s is not supported", 
                    shoonyaExchange.name()));
        }
    }
    
    public ShoonyaExchange toShoonyaExchange(Exchange exchange) {
        if(null == exchange) return null;
        switch(exchange) {
            case BSE: return ShoonyaExchange.BSE;
            case CDS: return ShoonyaExchange.CDS;
            case MCX: return ShoonyaExchange.MCX;
            case NFO: return ShoonyaExchange.NFO;
            case NSE: return ShoonyaExchange.NSE;
            default: throw new IllegalArgumentException(
                    String.format("Exchange %s is not supported by shoonya", exchange.name()));
        }
    }
    
    public ShoonyaExchange toShoonyaExchange(String text) {
        if(null == text || text.isBlank() || "null".equalsIgnoreCase(text)) return null;
        return ShoonyaExchange.valueOf(text);
    }
    
    public String toShoonyaExchangeString(ShoonyaExchange shoonyaExchange) {
        if(null == shoonyaExchange) return null;
        return shoonyaExchange.name();
    }
    
}
