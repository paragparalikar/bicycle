package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteOrderStatus;
import com.bicycle.core.order.OrderStatus;

public class KiteOrderStatusMapper {
    
    public OrderStatus toOrderStatus(KiteOrderStatus orderStatus) {
        if(null == orderStatus) return null;
        switch(orderStatus) {
        case VALIDATION_PENDING:
        case TRIGGER_PENDING:
        case AMO_REQ_RECEIVED:
        case PUT_ORDER_REQUEST_RECEIVED:
        case OPEN_PENDING: return OrderStatus.OPEN;
        case MODIFY_PENDING:
        case MODIFY_VALIDATION_PENDING: 
        case CANCEL_PENDING:
        case OPEN: return OrderStatus.OPEN;
        case CANCELLED: return OrderStatus.CANCELLED;
        case COMPLETE: return OrderStatus.COMPLETE;
        case REJECTED: return OrderStatus.REJECTED;
        default:throw new IllegalArgumentException(String.format("KiteOrderStatus %s is not supported", orderStatus.name()));
        }
    }

}
