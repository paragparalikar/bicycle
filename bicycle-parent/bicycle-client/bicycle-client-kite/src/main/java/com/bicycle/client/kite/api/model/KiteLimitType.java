package com.bicycle.client.kite.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum KiteLimitType {

	MARKET, LIMIT, SL, 
	
	@JsonProperty("SL-M")
	SLM;
	
}
