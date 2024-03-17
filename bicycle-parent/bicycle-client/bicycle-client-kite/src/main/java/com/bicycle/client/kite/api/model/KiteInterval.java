package com.bicycle.client.kite.api.model;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KiteInterval {

    M1("minute", Duration.ofMinutes(1), 30), 
    M3("3minute", Duration.ofMinutes(3), 90), 
    M5("5minute", Duration.ofMinutes(5), 90), 
    M10("10minute", Duration.ofMinutes(10), 90), 
    M15("15minute", Duration.ofMinutes(15), 180), 
    M30("30minute", Duration.ofMinutes(30), 180), 
    H("60minute", Duration.ofHours(1), 365), 
    H2("2hour", Duration.ofHours(2), 365), 
    H3("3hour", Duration.ofHours(3), 365), 
    D("day", Duration.ofDays(1), 2000);
    
    private final String text;
    private final Duration duration;
    private final int historicalBatchLimitInDays;

}
