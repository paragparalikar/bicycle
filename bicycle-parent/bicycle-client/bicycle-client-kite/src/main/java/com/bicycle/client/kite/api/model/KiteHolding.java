package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KiteHolding {

	private String tradingsymbol;
	
	private KiteExchange exchange;
	
	private int instrumentToken;
	
	private String isin;
	
	private KiteProduct product;
	
	private float price;
	
	private int quantity;
	
	private int t1Quantity;
	
	private int realisedQuantity;
	
	private int collateralQuantity;
	
	private String collateralType;
	
	private boolean discrepancy;
	
	private float averagePrice;
	
	private float lastPrice;
	
	private float closePrice;
	
	private float pnl;
	
	private float dayChange;
	
	private float dayChangePercentage;
	
}
