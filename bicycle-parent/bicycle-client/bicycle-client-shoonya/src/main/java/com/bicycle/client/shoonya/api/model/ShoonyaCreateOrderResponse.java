package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShoonyaCreateOrderResponse implements ShoonyaResponse {
    
    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("emsg") private String message;
    @JsonProperty("norenordno") private String orderId; 

}
