package com.bicycle.client.kite.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KiteTick {

    private int token;
    @JsonProperty("lastTradedPrice")
    private float lastTradedPrice;
    @JsonProperty("lastTradedTime")
    private long lastTradedTime;
    @JsonProperty("volumeTradedToday")
    private int volumeTradedToday;
    
/*    private String mode;
    private boolean tradable;
    @JsonProperty("highPrice")
    private float highPrice;
    @JsonProperty("lowPrice")
    private float lowPrice;
    @JsonProperty("openPrice")
    private float openPrice;
    @JsonProperty("closePrice")
    private float closePrice;
    private float change;
    @JsonProperty("lastTradeQuantity")
    private int lastTradedQuantity;
    @JsonProperty("averageTradePrice")
    private float averageTradePrice;
    @JsonProperty("totalBuyQuantity")
    private float totalBuyQuantity;
    @JsonProperty("totalSellQuantity")
    private float totalSellQuantity;
    private float oi;
    @JsonProperty("openInterestDayHigh")
    private float oiDayHigh;
    @JsonProperty("openInterestDayLow")
    private float oiDayLow;
    @JsonProperty("tickTimestamp")
    private long tickTimestamp;
    private KiteMarketDepth depth; */

}
