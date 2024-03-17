package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaCreateOrderRequest {

    @JsonProperty("remarks") private String remarks;
    @JsonProperty("uid") private String userId;
    @JsonProperty("actid") private String accountId;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("tsym") private String symbolName;
    @JsonProperty("prc") private double price;
    @JsonProperty("trgprc") private double triggerPrice;
    @JsonProperty("bpprc") private double bookProfitPrice;
    @JsonProperty("blprc") private double bookLossPrice;
    @JsonProperty("trailprc") private double trailingPrice;
    @JsonProperty("qty") private int quantity;
    @JsonProperty("dscqty") private int disclosedQuantity;
    @JsonProperty("prd") private String product;
    @JsonProperty("trantype") private String orderType;
    @JsonProperty("prctyp") private String limitType;
    @JsonProperty("ret") private String validity;
    @JsonProperty("ordersource") private String orderSource;
    @JsonProperty("amo") private String afterMarket;    
    
    public ShoonyaCreateOrderRequest(ShoonyaOrder order, String userId, String accountId) {
        this.userId = userId;
        this.accountId = accountId;
        this.remarks = order.getRemarks();
        this.exchange = order.getExchange();
        this.symbolName = order.getSymbolName();
        this.price = order.getPrice();
        this.triggerPrice = order.getTriggerPrice();
        this.bookProfitPrice = order.getBookProfiltPrice();
        this.bookLossPrice = order.getBookLossPrice();
        this.trailingPrice = order.getTrailPrice();
        this.quantity = order.getQuantity();
        this.disclosedQuantity = order.getDisclosedQuantity();
        this.product = order.getProduct();
        this.orderType = order.getOrderType();
        this.limitType = order.getLimitType();
        this.validity = order.getValidity();
        this.afterMarket = order.getAfterMarket();
        this.orderSource = "API";
    }
}
