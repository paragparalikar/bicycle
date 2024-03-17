package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteTransactionType;
import com.bicycle.core.order.OrderType;

public class KiteTransactionTypeMapper {
    
    public OrderType toOrderType(KiteTransactionType kiteTransactionType) {
        if(null == kiteTransactionType) return null;
        switch(kiteTransactionType) {
        case BUY: return OrderType.BUY;
        case SELL: return OrderType.SELL;
        default: throw new IllegalArgumentException(String.format("KiteTransactionType %s is not supported", kiteTransactionType.name()));
        }
    }
    
    public KiteTransactionType toKiteTransactionType(OrderType orderType) {
        if(null == orderType) return null;
        switch(orderType) {
        case BUY: return KiteTransactionType.BUY;
        case SELL: return KiteTransactionType.SELL;
        default: throw new IllegalArgumentException(String.format("OrderType %s is not supported by kite", orderType.name()));
        }
    }
}
