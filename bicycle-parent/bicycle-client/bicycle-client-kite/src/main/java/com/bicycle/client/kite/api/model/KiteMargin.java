package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KiteMargin {

	private KiteSegmentMargin equity;
	
	private KiteSegmentMargin commodity;
	
}
