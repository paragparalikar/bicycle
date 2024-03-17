package com.bicycle.client.kite.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum KiteOrderStatus {

	@JsonProperty("PUT ORDER REQUEST RECEIVED")
	PUT_ORDER_REQUEST_RECEIVED,
	
	@JsonProperty("VALIDATION PENDING")
	VALIDATION_PENDING,
	
	@JsonProperty("OPEN PENDING")
	OPEN_PENDING,

	OPEN,
	
	@JsonProperty("MODIFY VALIDATION PENDING")
	MODIFY_VALIDATION_PENDING,
	
	@JsonProperty("MODIFY PENDING")
	MODIFY_PENDING,

	@JsonProperty("TRIGGER PENDING")
	TRIGGER_PENDING,

	CANCELLED,
	
	REJECTED,
	
	COMPLETE,
	
	@JsonProperty("CANCEL PENDING")
	CANCEL_PENDING,

	@JsonProperty("AMO REQ RECEIVED")
	AMO_REQ_RECEIVED;
	
}
