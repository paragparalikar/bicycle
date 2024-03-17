package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.LimitType;

public class ShoonyaLimitTypeMapper {
    
    public LimitType toLimitType(String shoonyaLimitType) {
        if(null == shoonyaLimitType) return null;
        switch(shoonyaLimitType) {
            case "LMT" : return LimitType.LIMIT;
            case "MKT" : return LimitType.MARKET;
            case "SL-LMT" : return LimitType.SL;
            case "SL-MKT" : return LimitType.SLM;
            default : throw new IllegalArgumentException(
                    String.format("shoonyaLimitType %s is not supported", shoonyaLimitType));
        }
    }
    
    public String toShoonyaLimitType(LimitType limitType) {
        if(null == limitType) return null;
        switch(limitType) {
            case LIMIT: return "LMT";
            case MARKET: return "MKT";
            case SL: return "SL-LMT";
            case SLM: return "SL-MKT";
            default: throw new IllegalArgumentException(
                    String.format("LimitType %s is not supported by shoonya", limitType.name()));
        }
    }

}
