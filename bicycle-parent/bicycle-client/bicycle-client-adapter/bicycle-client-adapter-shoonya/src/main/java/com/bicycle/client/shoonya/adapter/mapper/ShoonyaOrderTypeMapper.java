package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.OrderType;

public class ShoonyaOrderTypeMapper {
    
    public OrderType toOrderType(String shoonyaOrderType) {
        if(null == shoonyaOrderType) return null;
        switch(shoonyaOrderType.toUpperCase()) {
            case "B" : return OrderType.BUY;
            case "S" : return OrderType.SELL;
            default : throw new IllegalArgumentException(
                    String.format("shoonyaOrderType %s is not supported", shoonyaOrderType));
        }
    }
    
    public String toShoonyaOrderType(OrderType orderType) {
        if(null == orderType) return null;
        switch(orderType) {
            case BUY: return "B";
            case SELL: return "S";
            default: throw new IllegalArgumentException(
                    String.format("OrderType %s is not supported by shoonya", orderType.name()));
        }
    }

}
