package com.bicycle.client.shoonya.api.model;

import com.bicycle.client.shoonya.api.ShoonyaTokens;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Value;

@Value
public class ShoonyaLoginResponse implements ShoonyaResponse {

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("susertoken") private String token;
    @JsonProperty("lastaccesstime") private Date lastAccessTime;
    @JsonProperty("spasswordreset") private String passwordResetRequired;
    @JsonProperty("exarr") private List<ShoonyaExchange> exchanges;
    @JsonProperty("uname") private String userName;
    @JsonProperty("prarr") private List<ShoonyaProductResponse> products;
    @JsonProperty("actid") private String accountId;
    @JsonProperty("email") private String email;
    @JsonProperty("uid") private String userId; 
    @JsonProperty("brkname") private String brokerName;
    @JsonProperty("emsg") private String message;
    
    public ShoonyaTokens getTokens() {
        return new ShoonyaTokens(token, userId, accountId);
    }
    
}
