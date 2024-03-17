package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;

@Value
public class ShoonyaHolding {

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("exch_tsym") private List<ShoonyaSymbol> symbols;
    @JsonProperty("holdqty") private int holdingQuantity;
    @JsonProperty("dpqty") private int dpHoldingQuantity;
    @JsonProperty("npoadqty") private int nonPoaDisplayQuantity;
    @JsonProperty("colqty") private int collateralQuantity;
    @JsonProperty("benqty") private int beneficieryQuantity;
    @JsonProperty("unplgdqty") private int unpledgedQuantity;
    @JsonProperty("brkcolqty") private int brokerCollateralQuantity;
    @JsonProperty("btstqty") private int btstQuantity;
    @JsonProperty("btstcolqty") private int btstCollateralQuantity;
    @JsonProperty("usedqty") private int usedQuantity;
    @JsonProperty("upldprc") private float uploadedAvgPrice;
    
}
