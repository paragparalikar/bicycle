package com.bicycle.client.shoonya.api.model;

import com.bicycle.client.shoonya.credentials.ShoonyaCredentials;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.Value;
import org.apache.commons.codec.digest.DigestUtils;

@Value
public class ShoonyaLoginRequest {
    
    @JsonProperty("uid") private String uid;
    @JsonProperty("imei") private String imei;
    @JsonProperty("pwd") private String password;
    @JsonProperty("vc") private String vendorCode;
    @JsonProperty("source") private String source;
    @JsonProperty("factor2") private String factor2;
    @JsonProperty("apkversion") private String apkVersion;
    @JsonProperty("appkey") private String applicationKey;
    
    public ShoonyaLoginRequest(ShoonyaCredentials credentials) {
        this.source = "API";
        this.imei = credentials.imei();
        this.uid = credentials.username();
        this.apkVersion = credentials.version();
        this.vendorCode = credentials.vendorCode();
        this.password = DigestUtils.sha256Hex(credentials.password());
        this.factor2 = String.format("%06d", new GoogleAuthenticator().getTotpPassword(credentials.pin()));
        this.applicationKey = DigestUtils.sha256Hex(String.join("|", credentials.username(), credentials.applicationKey()));
    }
    
}
