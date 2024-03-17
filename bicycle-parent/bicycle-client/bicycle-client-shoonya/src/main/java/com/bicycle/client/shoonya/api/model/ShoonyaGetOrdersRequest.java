package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoonyaGetOrdersRequest {

    @JsonProperty("uid") private String userId;
    @JsonProperty("prd") private String product; 
    
}
