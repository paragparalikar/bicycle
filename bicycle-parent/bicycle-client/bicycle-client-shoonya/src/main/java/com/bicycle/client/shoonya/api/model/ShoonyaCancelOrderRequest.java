package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoonyaCancelOrderRequest {

    @JsonProperty("uid") private String userId;
    @JsonProperty("norenordno") private String orderId; 
    
}
