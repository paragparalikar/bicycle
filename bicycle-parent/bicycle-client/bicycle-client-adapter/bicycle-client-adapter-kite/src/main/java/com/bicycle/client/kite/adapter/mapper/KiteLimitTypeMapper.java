package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteLimitType;
import com.bicycle.core.order.LimitType;

public class KiteLimitTypeMapper {

    public LimitType toLimitType(KiteLimitType kiteLimitType) {
        if(null == kiteLimitType) return null;
        switch(kiteLimitType){
        case LIMIT:return LimitType.LIMIT;
        case MARKET:return LimitType.MARKET;
        case SL:return LimitType.SL;
        case SLM:return LimitType.SLM;
        default:throw new IllegalArgumentException(String.format("KiteLimitType %s is not supported", kiteLimitType.name()));
        }
    }
    
    public KiteLimitType toKiteLimitType(LimitType limitType) {
        if(null == limitType) return null;
        switch(limitType){
        case LIMIT:return KiteLimitType.LIMIT;
        case MARKET:return KiteLimitType.MARKET;
        case SL:return KiteLimitType.SL;
        case SLM:return KiteLimitType.SLM;
        default:throw new IllegalArgumentException(String.format("LimitType %s is not supported by kite", limitType.name()));
        }
    }
    
}
