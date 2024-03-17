package com.bicycle.client.kite.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"exchange", "exchangeToken"})
public class KiteSymbol {

	private int instrumentToken;
	private int exchangeToken;
	private String tradingsymbol;
	private String name;
	private float lastPrice;
	private String expiry;
	private float strike;
	private float tickSize;
	private int lotSize;
	private String type;
	private String segment;
	private KiteExchange exchange;
	
	public KiteSymbol(String text) {
	    final String[] tokens = text.split(",");
	    this.type = tokens[9];
	    this.name = tokens[3];
	    this.expiry = tokens[5];
	    this.segment = tokens[10];
	    this.tradingsymbol = tokens[2];
	    this.exchange = KiteExchange.valueOf(tokens[11]);
	    this.exchangeToken = Integer.parseInt(tokens[1]);
	    this.instrumentToken = Integer.parseInt(tokens[0]);
	    this.strike = null == tokens[6] ? 0 : Float.parseFloat(tokens[6]);
	    this.lotSize = null == tokens[8] ? 0 : Integer.parseInt(tokens[8]);
	    this.tickSize = null == tokens[7] ? 0 : Float.parseFloat(tokens[7]);
	    this.lastPrice = null == tokens[4] ? 0 : Float.parseFloat(tokens[4]);
	}
	
	@Override
	public String toString() {
	    return String.join(",", 
                String.valueOf(instrumentToken),
                String.valueOf(exchangeToken),
                tradingsymbol,
                unquote(name),
                String.valueOf(lastPrice),
                expiry,
                String.valueOf(strike),
                String.valueOf(tickSize),
                String.valueOf(lotSize),
                type,
                segment,
                exchange.name());
	}
	
	public String unquote(String text) {
        return null != text && 0 < text.trim().length() ? text.replaceAll("\"", "").trim() : text;
    }
	
}
