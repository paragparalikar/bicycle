package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaPosition {

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("tsym") private String symbolName;
    @JsonProperty("token") private String token;
    @JsonProperty("uid") private String userId;
    @JsonProperty("actid") private String accountId;
    @JsonProperty("prd") private String product;
    @JsonProperty("netqty") private int netQuantity;
    @JsonProperty("netavgprc") private float netAveragePrice;
    @JsonProperty("daybuyqty") private int dayBuyQuantity;
    @JsonProperty("daysellqty") private int daySellQuantity;
    @JsonProperty("daybuyavgprc") private float dayBuyAveragePrice;
    @JsonProperty("daysellavgprc") private float daySellAveragePrice;
    @JsonProperty("daybuyamt") private float dayBuyAmount;
    @JsonProperty("daysellamt") private float daySellAmount;
    @JsonProperty("cfbuyqty") private int carryForwardBuyQuantity;
    @JsonProperty("cforgavgprc") private float originalAveragePrice;
    @JsonProperty("cfsellqty") private int carryForwardSellQuantity;
    @JsonProperty("cfbuyavgprc") private float carryForwardBuyAveragePrice;
    @JsonProperty("cfsellavgprc") private float carryForwardSellAveragePrice;
    @JsonProperty("cfbuyamt") private float carryForwardBuyAmount;
    @JsonProperty("cfsellamt") private float carryForwardSellAmount;
    @JsonProperty("lp") private float lastPrice;
    @JsonProperty("rpnl") private float realizedProfitLoss;
    @JsonProperty("urmtom") private float unrealizedProfitLoss;
    @JsonProperty("bep") private float breakEvenPrice;
    @JsonProperty("openbuyqty") private int openBuyQuantity;
    @JsonProperty("opensellqty") private int openSellQuantity;
    @JsonProperty("openbuyamt") private float openBuyAmount;
    @JsonProperty("opensellamt") private float openSellAmount;
    @JsonProperty("openbuyavgprc") private float openBuyAveragePrice;
    @JsonProperty("opensellavgprc") private float openSellAveragePrice;
    
    
}
