package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KiteAvailableMargin {

	private double adhocMargin;
	
	private double cash;
	
	private double openingBalance;
	
	private double liveBalance;
	
	private double collateral;
	
	private double intradayPayin;
	
}
