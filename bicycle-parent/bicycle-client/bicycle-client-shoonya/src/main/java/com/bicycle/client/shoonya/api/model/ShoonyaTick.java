package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaTick {
    
    @JsonProperty("e") private ShoonyaExchange exchange;
    @JsonProperty("tk") private int token;
    @JsonProperty("lp") private float lastTradedPrice;
    @JsonProperty("pc") private float percentageChange;
    @JsonProperty("v") private int volume;
    @JsonProperty("o") private float openPrice;
    @JsonProperty("h") private float highPrice;
    @JsonProperty("l") private float lowPrice;
    @JsonProperty("c") private float closePrice;
    @JsonProperty("c") private float averageTradePrice;
    @JsonProperty("oi") private int openInterest;
    @JsonProperty("poi") private int previousOpenInterest;
    @JsonProperty("toi") private int totalOpenIterest;
    @JsonProperty("bq1") private int bestBuyQuantity;
    @JsonProperty("bp1") private float bestBuyPrice;
    @JsonProperty("sq1") private int bestSellQuantity;
    @JsonProperty("sp1") private float bestSellPrice; 
}
