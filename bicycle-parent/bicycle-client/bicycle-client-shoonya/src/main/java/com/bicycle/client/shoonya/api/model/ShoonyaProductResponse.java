package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;

@Value
public class ShoonyaProductResponse {

    @JsonProperty("prd") private String name;
    @JsonProperty("s_prdt_ali") private String displayName;
    @JsonProperty("exch") private List<ShoonyaExchange> exchanges;
    
}
