package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShoonyaStatusResponse {

    @JsonProperty("Ok") OK, 
    @JsonProperty("Not_OK") NOT_OK;
    
}
