package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteOrderValidity;
import com.bicycle.core.order.Validity;

public class KiteOrderValidityMapper {

    public Validity toValidity(KiteOrderValidity kiteOrderValidity) {
        if(null == kiteOrderValidity) return null;
        switch(kiteOrderValidity) {
        case DAY:return Validity.DAY;
        case IOC:return Validity.IOC;
        default:throw new IllegalArgumentException(String.format("KiteOrderValidity %s is not supported", kiteOrderValidity.name()));
        }
    }
    
    public KiteOrderValidity toKiteOrderValidity(Validity validity) {
        if(null == validity) return null;
        switch(validity) {
        case DAY:return KiteOrderValidity.DAY;
        case IOC:return KiteOrderValidity.IOC;
        default:throw new IllegalArgumentException(String.format("Validity %s is not supported by kite", validity.name()));
        }
    }
    
}
