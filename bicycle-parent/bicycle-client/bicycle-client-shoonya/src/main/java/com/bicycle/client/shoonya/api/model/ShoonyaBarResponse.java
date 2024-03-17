package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShoonyaBarResponse extends ArrayList<ShoonyaBar> implements ShoonyaResponse {
    private static final long serialVersionUID = 1L;

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("emsg") private String message;
    
}
