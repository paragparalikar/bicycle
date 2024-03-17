package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.OrderStatus;

public class ShoonyaOrderStatusMapper {

    // PENDING, REJECTED, 
    
    public OrderStatus toOrderStatus(String shoonyaOrderStatus) {
        if(null == shoonyaOrderStatus) return null;
        switch(shoonyaOrderStatus.toUpperCase()) {
            case "NEW" :
            case "PENDING" :
            case "OPEN" :
            case "REPLACED" : return OrderStatus.OPEN;
            case "COMPLETE" : return OrderStatus.COMPLETE;
            case "CANCELLED" : return OrderStatus.CANCELLED;
            case "REJECTED" : return OrderStatus.REJECTED;
            default : throw new IllegalArgumentException(String.format(
                    "shoonyaOrderStatus %s is not supported", shoonyaOrderStatus));
        }
    }
    
}
