package com.bicycle.client.kite.api.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class KiteOrder {

	private int instrumentToken;
	private String orderId;
	private String parentOrderId;	
	private String exchangeOrderId;
	private String placedBy;
	private boolean modified;
	private KiteOrderVariety variety;
	private float averagePrice;
	private int pendingQuantity;
	private int filledQuantity;
	private int cancelledQuantity;
	private int marketProtection;
	private String orderTimestamp;
	private String exchangeTimestamp;
	private String exchangeUpdateTimestamp;
	private String statusMessage;
	private String statusMessageRaw;
	private KiteOrderStatus status;
	private Map<String, Object> meta;
	private String tag;
	private List<String> tags;
	private String guid;
	
	// REGULAR
	private String tradingsymbol;
	private KiteExchange exchange;
	private KiteTransactionType transactionType;
	private KiteLimitType limitType;
	private int quantity;
	private KiteProduct product;
	private float price;
	private float triggerPrice;
	private int disclosedQuantity;
	private KiteOrderValidity validity;
	private int validityTtl;
	
	
	//BO
	private float squareoff;
	private float stoploss;
	private float trailingStoploss;
	
}
