package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.Validity;

public class ShoonyaValidityMapper {
    
    public Validity toValidity(String shoonyaValidity) {
        if(null == shoonyaValidity) return null;
        switch(shoonyaValidity) {
            case "DAY" : return Validity.DAY;
            case "IOC" : return Validity.IOC;
            default : throw new IllegalArgumentException(
                    String.format("shoonyaValidity %s is not supported", shoonyaValidity));
        }
    }
    
    public String toShoonyaValidity(Validity validity) {
        if(null == validity) return null;
        switch(validity) {
            case DAY : return "DAY";
            case IOC : return "IOC";
            default : throw new IllegalArgumentException(
                    String.format("Validity %s is not supported by shoonya", validity));
        }
    }

}
