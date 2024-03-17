package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteOrderVariety;
import com.bicycle.core.order.Variety;

public class KiteOrderVarietyMapper {
    
    public Variety toVariety(KiteOrderVariety orderVariety) {
        if(null == orderVariety) return null;
        switch(orderVariety) {
        case AMO: return Variety.AMO;
        case REGULAR: return Variety.REGULAR;
        default: throw new IllegalArgumentException(String.format("KiteOrderVariety %s is not supported", orderVariety));
        }
    }
    
    public KiteOrderVariety toKiteOrderVariety(Variety tradeVariety) {
        if(null == tradeVariety) return null;
        switch(tradeVariety) {
        case AMO:return KiteOrderVariety.AMO;
        case REGULAR:return KiteOrderVariety.REGULAR;
        default: throw new IllegalArgumentException(String.format("Variety %s is not supported by kite", tradeVariety.name()));
        }
    }

}
