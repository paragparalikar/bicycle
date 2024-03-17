package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KiteSegmentMargin {

	private boolean enabled;
	private double net;
	private KiteAvailableMargin available;
	private KiteUtilizedMargin utilised;
	
}
