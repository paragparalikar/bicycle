package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.Variety;

public class ShoonyaAfterMarketMapper {
    
    public Variety toVariety(String shoonyaAfterMarket) {
        return null != shoonyaAfterMarket 
                && "YES".equalsIgnoreCase(shoonyaAfterMarket) ? 
                Variety.AMO : Variety.REGULAR;
    }
    
    public String toShoonyaAfterMarket(Variety variety) {
        if(null == variety) return "No";
        switch(variety) {
            case AMO: return "Yes";
            case REGULAR:return "No";
            default: throw new IllegalArgumentException(
                    String.format("Variety %s is not supported by shoonya", variety.name()));
        }
    }

}
