package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoonyaBarRequest {

    @JsonProperty("uid") private String userId;
    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("token") private int token;
    @JsonProperty("st") private long startTime;
    @JsonProperty("et") private long endTime;
    @JsonProperty("intrv") private int intervalInMinutes;
    
}
