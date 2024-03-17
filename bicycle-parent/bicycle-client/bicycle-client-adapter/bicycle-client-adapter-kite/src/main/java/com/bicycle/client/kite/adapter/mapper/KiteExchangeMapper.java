package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.core.symbol.Exchange;

public class KiteExchangeMapper {
    
    public Exchange toExchange(KiteExchange exchange) {
        switch(exchange) {
            case BCD: return Exchange.BCD;
            case BFO: return Exchange.BFO;
            case BSE: return Exchange.BSE;
            case CDS: return Exchange.CDS;
            case MCX: return Exchange.MCX;
            case MF : return Exchange.MF;
            case NFO: return Exchange.NFO;
            case NSE: return Exchange.NSE;
            default: throw new IllegalArgumentException(String.format(
                    "Kite exchange %s not supported", exchange.name()));
        }
    }
    
    public KiteExchange toKiteExchange(Exchange exchange) {
        switch(exchange) {
            case BCD: return KiteExchange.BCD;
            case BFO: return KiteExchange.BFO;
            case BSE: return KiteExchange.BSE;
            case CDS: return KiteExchange.CDS;
            case MCX: return KiteExchange.MCX;
            case MF : return KiteExchange.MF;
            case NFO: return KiteExchange.NFO;
            case NSE: return KiteExchange.NSE;
            default: throw new IllegalArgumentException(String.format(
                    "Exchange %s not supported by kite", exchange.name()));
        }
    }
    
    public KiteExchange toKiteExchange(String text) {
        if(null == text) return null;
        return KiteExchange.valueOf(text);
    }
    
    public String toKiteExchangeString(KiteExchange kiteExchange) {
        return kiteExchange.name();
    }

}
