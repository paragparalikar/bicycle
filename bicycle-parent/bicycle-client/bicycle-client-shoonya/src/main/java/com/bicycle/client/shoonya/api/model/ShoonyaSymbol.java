package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
@EqualsAndHashCode(of = {"exchange", "token"})
public class ShoonyaSymbol {

    @JsonProperty("exch") private ShoonyaExchange exchange;
    @JsonProperty("tsym") private String code;
    @JsonProperty("token") private int token;
    private String name;
    private String type;
    private int lotSize;
    private float tickSize;
    
    public ShoonyaSymbol(String text) {
        final String[] tokens = text.split(",");
        this.code = tokens[3];
        this.name = tokens[4];
        this.type = tokens[5];
        this.token = Integer.parseInt(tokens[1]);
        this.lotSize = Integer.parseInt(tokens[2]);
        this.tickSize = Float.parseFloat(tokens[6]);
        this.exchange = null == tokens[0] || tokens[0].isBlank() || "null".equalsIgnoreCase(tokens[0]) ? 
                null : ShoonyaExchange.valueOf(tokens[0]);
    }
    
    @Override
    public String toString() {
        return String.join(",", 
                null == exchange ? "" : exchange.name(),
                code,
                String.valueOf(token),
                name,
                type,
                String.valueOf(lotSize),
                String.valueOf(tickSize));
    }
    
}
