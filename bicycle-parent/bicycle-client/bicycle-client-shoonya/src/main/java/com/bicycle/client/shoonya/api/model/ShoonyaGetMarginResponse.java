package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaGetMarginResponse implements ShoonyaResponse {

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("emsg") private String message; 
    @JsonProperty("actid") private String accountId;
    @JsonProperty("prd") private String product;
    @JsonProperty("seg") private String segment;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("cash") private float cash;
    @JsonProperty("payin") private float payin;
    @JsonProperty("payout") private float payout;
    @JsonProperty("brkcollamt") private float brokerCollateralAmount;
    @JsonProperty("unclearedcash") private float unclearedCash;
    @JsonProperty("daycash") private float dayCash;
    @JsonProperty("marginused") private float marginUsed;
    @JsonProperty("mtomcurper") private float m2mCurrentPercentage;
    @JsonProperty("cbu") private float cacBuyUsed;
    @JsonProperty("csc") private float cacSellUsed;
    @JsonProperty("rpnl") private float realizedProfitLoss;
    @JsonProperty("unmtom") private float unrealizedM2m;
    @JsonProperty("marprt") private float coveredProductMargin;
    @JsonProperty("span") private float span;
    @JsonProperty("expo") private float exposureMargin;
    @JsonProperty("premium") private float premiumUsed;
    @JsonProperty("varelm") private float varElmMargin;
    @JsonProperty("grexpo") private float grossExposure;
    @JsonProperty("greexpo_d") private float grossExposureDerivatives;
    @JsonProperty("scripbskmar") private float scripBasketMargin;
    @JsonProperty("addscripbskmar") private float additionalScripBasketMargin;
    @JsonProperty("brokerage") private float brokerageAmount;
    @JsonProperty("collateral") private float collateralMargin;
    @JsonProperty("grcoll") private float grossCollateral;
    @JsonProperty("turnoverlmt") private float turnoverLimit;
    @JsonProperty("pendordval") private float pendingOrderValue;
    
    
}
