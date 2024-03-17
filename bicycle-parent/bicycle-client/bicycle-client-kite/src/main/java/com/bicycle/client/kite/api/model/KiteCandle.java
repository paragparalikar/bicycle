package com.bicycle.client.kite.api.model;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KiteCandle {

	private final ZonedDateTime timestamp;
	private final int volume, openInterest;
	private final float open, high, low, close;
	
}
