package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KitePosition {

	private String tradingsymbol;
	private KiteExchange exchange;
	private int instrumentToken;
	private KiteProduct product;
	private int quantity;
	private int overnightQuantity;
	private int multiplier;
	private float averagePrice;
	private float closePrice;
	private float lastPrice;
	private float value;
	private float pnl;
	private float m2m;
	private float realised;
	private float unrealised;
	private int buyQuantity;
	private float buyPrice;
	private float buyValue;
	private float buyM2m;
	private int dayBuyQuantity;
	private float dayBuyPrice;
	private float dayBuyValue;
	private int sellQuantity;
	private float sellPrice;
	private float sellValue;
	private int daySellQuantity;
	private float daySellPrice;
	private float daySellValue;
	
}
