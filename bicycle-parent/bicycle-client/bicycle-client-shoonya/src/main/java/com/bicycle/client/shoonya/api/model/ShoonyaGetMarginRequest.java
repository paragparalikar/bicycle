package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoonyaGetMarginRequest {

    @JsonProperty("uid") private String userId;
    @JsonProperty("actid") private String accountId;
    @JsonProperty("prd") private String product;
    @JsonProperty("seg") private String segment;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    
}
