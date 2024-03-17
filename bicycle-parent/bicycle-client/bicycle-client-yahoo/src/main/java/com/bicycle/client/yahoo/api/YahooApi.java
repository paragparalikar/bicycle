package com.bicycle.client.yahoo.api;

import com.bicycle.client.yahoo.model.YahooBar;
import java.time.ZonedDateTime;
import java.util.List;

public interface YahooApi {

    List<YahooBar> getBars(String symbol, String interval, ZonedDateTime from, ZonedDateTime to);
    
}
