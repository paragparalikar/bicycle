package com.bicycle.client.yahoo.api;

import com.bicycle.client.yahoo.model.YahooBar;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;

public class YahooHttpApi {
    private static final LocalTime NSE_START_TIME = LocalTime.of(9, 15);
    private static final String URL_BASE = "https://query1.finance.yahoo.com/";
    private static final String URL_CHART = "v8/finance/chart/";
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @SneakyThrows
    public List<YahooBar> getBars(String symbol, String timeframe, ZonedDateTime from, ZonedDateTime to) {
        final StringBuilder builder = new StringBuilder(URL_BASE + URL_CHART + symbol);
        builder.append("?period1=").append(String.valueOf(from.toEpochSecond()));
        builder.append("&period2=").append(String.valueOf(to.toEpochSecond()));
        builder.append("&interval=").append(timeframe);
        builder.append("&includePrePost=false&events=&lang=en-US&region=US");
        final HttpURLConnection con = (HttpURLConnection) new URL(builder.toString()).openConnection();
        
        con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
        con.setRequestProperty("Accept","*/*");
        
        final TreeNode node = objectMapper.createParser(con.getInputStream()).readValueAsTree();
        final JsonNode error = (JsonNode) node.get("chart").get("error");
        
        if(null != error && !error.isNull()) {
            System.err.println(error);
            return Collections.emptyList();
        }
        
        final ArrayNode results = (ArrayNode) node.get("chart").get("result");
        final ArrayNode timestamps = (ArrayNode) results.get(0).get("timestamp");
        
        if(null == timestamps) {
            return Collections.emptyList();
        }
        
        final ArrayNode quotes = (ArrayNode) results.get(0).get("indicators").get("quote");
        final ArrayNode opens = (ArrayNode) quotes.get(0).get("open");
        final ArrayNode highs = (ArrayNode) quotes.get(0).get("high");
        final ArrayNode lows = (ArrayNode) quotes.get(0).get("low");
        final ArrayNode closes = (ArrayNode) quotes.get(0).get("close");
        final ArrayNode volumes = (ArrayNode) quotes.get(0).get("volume");
        
        final List<YahooBar> bars = new ArrayList<>(timestamps.size());
        
        for(int index = 0; index < timestamps.size(); index++) {
            final ZonedDateTime timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(
                    timestamps.get(index).asLong()), ZoneId.systemDefault());
            final ZonedDateTime modifiedTimestamp = ZonedDateTime.of(timestamp.toLocalDate(), 
                    NSE_START_TIME, ZoneId.systemDefault());
            
            final YahooBar bar = YahooBar.builder()
                    .date(modifiedTimestamp.toInstant().toEpochMilli())
                    .open(opens.get(index).floatValue())
                    .high(highs.get(index).floatValue())
                    .low(lows.get(index).floatValue())
                    .close(closes.get(index).floatValue())
                    .volume(volumes.get(index).intValue())
                    .build();
            
            bars.add(bar);
        }
        
        return bars;
    }

}
