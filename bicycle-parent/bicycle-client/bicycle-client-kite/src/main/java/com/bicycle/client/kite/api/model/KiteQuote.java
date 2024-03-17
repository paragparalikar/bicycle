package com.bicycle.client.kite.api.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KiteQuote {

	private long instrumentToken;
	private LocalDateTime timestamp;
	private LocalDateTime lastTradeTime;
	private double lastPrice;
	private int lastQuantity;
	private int buyQuantity;
	private int sellQuantity;
	private long volume;
	private double averagePrice;
	private double oi;
	private double oiDayHigh;
	private double oiDayLow;
	private KiteOhlc ohlc;
	private double netChange;
	private double lowerCircuitLimit;
	private double upperCircuitLimit;
	private KiteMarketDepth depth;
	
}
