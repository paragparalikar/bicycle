package com.bicycle.client.kite.api.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class KiteCandleSeries {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	private List<List<Object>> candles;
	private final List<KiteCandle> data = new ArrayList<>();
	
	public List<KiteCandle> getData(){
		if(data.isEmpty() && null != candles) {
			candles.stream().map(this::parse).forEach(data::add);
		}
		return data;
	}
	
	private KiteCandle parse(List<Object> objects) {
		final int oi = (int) (objects.size() >= 7 ? ((Number)toDouble(objects.get(6))).longValue() : 0); 
		return KiteCandle.builder()
				.timestamp(ZonedDateTime.parse((String)objects.get(0), formatter))
				.open(toDouble(objects.get(1)))
				.high(toDouble(objects.get(2)))
				.low(toDouble(objects.get(3)))
				.close(toDouble(objects.get(4)))
				.volume(((Number) objects.get(5)).intValue())
				.openInterest(oi)
				.build();
	}
	
	private float toDouble(Object value) {
		if(null == value) return 0;
		if(value instanceof Float) return (Float) value;
		if(value instanceof Double) return ((Double) value).floatValue();
		if(value instanceof Integer) return ((Integer) value).floatValue();
		return 0;
	}
	
}
