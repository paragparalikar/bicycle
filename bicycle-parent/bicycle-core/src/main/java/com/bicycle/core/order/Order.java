package com.bicycle.core.order;

import com.bicycle.core.symbol.Symbol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Order {

    private String id; // broker id
    private String portfolioId;
    private String exchangeOrderId;
    private String tradingStrategyId;
    
    private Symbol symbol;
    private String message;
    private long timestamp;
    private OrderType type;
    private Product product;
    private Variety variety;
    private Validity validity;
    private LimitType limiType;
    private OrderStatus status;
    private int quantity, filledQuantity, cancelledQuantity, disclosedQuantity;
    private float price, triggerPrice, averagePrice, bookProfiltPrice, bookLossPrice, trailPrice;
    
    public void copy(Order order) {
        this.id = order.id;
        this.portfolioId = order.portfolioId;
        this.exchangeOrderId = order.exchangeOrderId;
        this.tradingStrategyId = order.tradingStrategyId;
        
        this.symbol = order.symbol;
        this.message = order.message;
        this.timestamp = order.timestamp;
        this.type = order.type;
        this.product = order.product;
        this.variety = order.variety;
        this.validity = order.validity;
        this.limiType = order.limiType;
        this.status = order.status;
        this.quantity = order.quantity;
        this.filledQuantity = order.filledQuantity;
        this.cancelledQuantity = order.cancelledQuantity;
        this.disclosedQuantity = order.disclosedQuantity;
        this.price = order.price;
        this.triggerPrice = order.triggerPrice;
        this.averagePrice = order.averagePrice;
        this.bookProfiltPrice = order.bookProfiltPrice;
        this.bookLossPrice = order.bookLossPrice;
        this.trailPrice = order.trailPrice;
    }
    
}
