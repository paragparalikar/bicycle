package com.bicycle.client.kite;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KiteConstant {
	
	public static final String APIKEY = "kitefront";
	public static final int TIMEOUT = 10000;
	public static final ObjectMapper JSON = new ObjectMapper();
	public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";
	public static final String USER_AGENT_KITE = "kite3-web";
	public static final String WS_VERSION = "3.0.13";
	
	public static final String URL_WS = "wss://ws.zerodha.com";
	public static final String URL_INSTRUMENTS = System.getProperty("com.whiteowl.client.kite.url.positions","https://api.kite.trade/instruments/");
	
	public static final String URL_ORDERS = System.getProperty("com.whiteowl.client.kite.url.orders","/oms/orders");
	public static final String URL_QUOTE = System.getProperty("com.whiteowl.client.kite.url.quote","/oms/quote");
	public static final String URL_QUOTE_OHLC = System.getProperty("com.whiteowl.client.kite.url.quote.ohlc","/oms/quote/ohlc");
	public static final String URL_QUOTE_LTP = System.getProperty("com.whiteowl.client.kite.url.quote.ltp","/oms/quote/ltp");
	public static final String URL_POSITIONS = System.getProperty("com.whiteowl.client.kite.url.positions","/oms/portfolio/positions");
	public static final String URL_PROFILE = System.getProperty("com.whiteowl.client.kite.url.profile","/oms/user/profile/full");
	public static final String URL_HOLDINGS = System.getProperty("com.whiteowl.client.kite.url.holdings","/oms/portfolio/holdings");
	public static final String URL_DASHBOARD = System.getProperty("com.whiteowl.client.kite.url.dashboard","/dashboard");
	public static final String URL_TWOFA = System.getProperty("com.whiteowl.client.kite.url.twofa","/api/twofa");
	public static final String URL_LOGIN = System.getProperty("com.whiteowl.client.kite.url.login","/api/login");
	public static final String URL_MARGIN = System.getProperty("com.whiteowl.client.kite.url.margin","/oms/user/margins");
	public static final String URL_BASE = System.getProperty("com.whiteowl.client.kite.url.base","https://kite.zerodha.com");
	public static final String URL_BARS = System.getProperty("com.whiteowl.client.kite.url.bars", "/oms/instruments/historical");
	
	public static final String INTERVAL_DAY = "day";
	public static final String INTERVAL_MINUTE = "minute";
	public static final String INTERVAL_3_MINUTE = "3minute";
	public static final String INTERVAL_5_MINUTE = "5minute";
	public static final String INTERVAL_10_MINUTE = "10minute";
	public static final String INTERVAL_15_MINUTE = "15minute";
	public static final String INTERVAL_30_MINUTE = "30minute";
	public static final String INTERVAL_60_MINUTE = "60minute";
	
	public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(KiteConstant.FORMAT_TIMESTAMP);
	
	
	static {
		KiteConstant.JSON.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		KiteConstant.JSON.setDateFormat(new SimpleDateFormat(FORMAT_TIMESTAMP));
		KiteConstant.JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	
}
