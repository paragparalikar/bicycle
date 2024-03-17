package com.bicycle.client.kite.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KiteResponse<T> {

	private T data;
	
	private String status;
	
	private String message;
	
	@JsonProperty("error_type")
	private String errorType;
	
}
