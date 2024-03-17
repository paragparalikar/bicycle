package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaUpdateOrderRequest {

    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("norenordno") private String orderId;
    @JsonProperty("prctyp") private String limitType;
    @JsonProperty("prc") private double price;
    @JsonProperty("qty") private int quantity;
    @JsonProperty("tsym") private String symbolName;
    @JsonProperty("ret") private String validity;
    @JsonProperty("trgprc") private double triggerPrice;
    @JsonProperty("uid") private String userId;
    @JsonProperty("bpprc") private double bookProfiltPrice;
    @JsonProperty("blprc") private double bookLossPrice;
    @JsonProperty("trailprc") private double trailPrice; 
    
    public ShoonyaUpdateOrderRequest(ShoonyaOrder order, String userId) {
        this.exchange = order.getExchange();
        this.orderId = order.getOrderId();
        this.limitType = order.getLimitType();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
        this.symbolName = order.getSymbolName();
        this.validity = order.getValidity();
        this.triggerPrice = order.getTriggerPrice();
        this.userId = order.getUserId();
        this.bookLossPrice = order.getBookLossPrice();
        this.bookProfiltPrice = order.getBookProfiltPrice();
        this.trailPrice = order.getTrailPrice();
    }
    
}
