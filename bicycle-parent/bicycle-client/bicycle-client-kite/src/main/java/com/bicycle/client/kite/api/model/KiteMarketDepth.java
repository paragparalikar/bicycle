package com.bicycle.client.kite.api.model;

import java.util.List;

import lombok.Data;

@Data
public class KiteMarketDepth {

	private List<KiteDepth> buy;
	private List<KiteDepth> sell;
	
}
