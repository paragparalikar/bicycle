package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ShoonyaOrder {
    
    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("uid") private String userId;
    @JsonProperty("actid") private String accountId;
    @JsonProperty("remarks") private String remarks;
    @JsonProperty("norenordno") private String orderId;
    @JsonProperty("exchordid") private String exchangeId;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("tsym") private String symbolName;
    @JsonProperty("prc") private float price;
    @JsonProperty("avgprc") private float averagePrice;
    @JsonProperty("trgprc") private float triggerPrice;
    @JsonProperty("bpprc") private float bookProfiltPrice;
    @JsonProperty("blprc") private float bookLossPrice;
    @JsonProperty("trailprc") private float trailPrice;
    @JsonProperty("qty") private int quantity;
    @JsonProperty("fillshares") private int filledQuantity;
    @JsonProperty("cancelqty") private int cancelledQuantity;
    @JsonProperty("dscqty") private int disclosedQuantity;
    @JsonProperty("prd") private String product;        //
    @JsonProperty("status") private String orderStatus; //
    @JsonProperty("trantype") private String orderType; //
    @JsonProperty("prctyp") private String limitType;   //
    @JsonProperty("ret") private String validity;       //
    @JsonProperty("amo") private String afterMarket;
    @JsonProperty("rejreason") private String rejectionReason;
    @JsonProperty("snoordt") private int snoOrderType;
    @JsonProperty("snonum") private int snoNum; 
    
}
